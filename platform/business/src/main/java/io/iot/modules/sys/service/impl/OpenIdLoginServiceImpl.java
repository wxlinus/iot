package io.iot.modules.sys.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.RRException;
import io.renren.common.utils.EncryptUtil;
import io.renren.modules.login.entity.TokenEntity;
import io.renren.modules.login.form.WxBindMobileForm;
import io.renren.modules.login.service.LoginService;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.MiniProgramService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.service.condition.OpenIdLoginCondition;
import io.renren.modules.sys.shiro.ShiroUtils;
import io.renren.modules.sys.shiro.WeChatToken;
import io.renren.modules.visitorcenter.entity.VisitorEntity;
import io.renren.modules.visitorcenter.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xupeng
 * 使用微信openid作为用户标识，处理微信等登陆服务
 */
@Slf4j
@Service("openIdLoginService")
@Conditional(OpenIdLoginCondition.class)
public class OpenIdLoginServiceImpl implements MiniProgramService {

	/**
	 * 配置读取
	 */
	@Autowired
	private Environment env;
	/**
	 * 用户服务
	 */
	@Autowired
	private VisitorService visitorService;
	/**
	 * 登陆服务
	 */
	@Autowired
	private LoginService loginService;
	/**
	 * 管理系统用户服务
	 */
	@Autowired
	private SysUserService sysUserService;
	
	/**   
	* @author xupeng
	* @date 2021-1-20 12:33:01
	* @Description:TODO 解析微信用户信息
	*/
	@Override
	public JSONObject code2Session(String jscode, String ymlCode, String encryptedData, String iv) {
		String appid = env.getProperty("mini-program."+ymlCode+".appid");
		String appsecret = env.getProperty("mini-program."+ymlCode+".appsecret");
		StringBuffer urlBuff = new StringBuffer();
		urlBuff.append("https://api.weixin.qq.com/sns/jscode2session?appid=")
		.append(appid)
		.append("&secret=")
		.append(appsecret)
		.append("&js_code=")
		.append(jscode)
		.append("&grant_type=authorization_code");
		String result = HttpUtil.get(urlBuff.toString());
		JSONObject jsonObject = JSONObject.parseObject(result);
		String sessionKey = jsonObject.getString("session_key");
		if (StringUtils.isBlank(sessionKey)) {
			log.error("jscode2session调用失败："+urlBuff.toString());
			log.error(jsonObject.toJSONString());
			throw new RRException("微信服务异常:"+jsonObject.toJSONString());
		}
		System.err.println(result);
		if (StringUtils.isBlank(jsonObject.getString("openid"))) {
			return EncryptUtil.getUserInfo(encryptedData,sessionKey,iv);
		}
		return jsonObject;
	}

	/**   
	* @author xupeng
	* @date 2021-1-20 12:33:01
	* @Description:TODO openid登陆
	*/
	@Override
	public TokenEntity wxLogin(String jscode, String ymlCode, String encryptedData, String iv) {
		JSONObject userInfo = code2Session(jscode, ymlCode, encryptedData, iv);
		String openId = userInfo.getString("openid");
		VisitorEntity visitor = visitorService.lambdaQuery().eq(VisitorEntity::getOpenId, openId).one();
		if (visitor==null) {
			log.error(openId+"未绑定手机");
			throw new RRException("请绑定手机号",1403);
		}
		TokenEntity token = loginService.doLogin(visitor);
		
		return token;
	}

	/**   
	* @author xupeng
	* @date 2021-1-20 12:33:01
	* @Description:TODO openid微信登录并绑定手机号
	*/
	@Override
	public TokenEntity wxBindMobileAndLogin(WxBindMobileForm form) {
		String jscode = form.getJscode();
		String ymlCode = form.getYmlCode();
		String encryptedData = form.getEncryptedData();
		String iv = form.getIv();
		String phoneNum = form.getPhoneNum();
		String smsCode = form.getSmsCode();
		if (StringUtils.isBlank(ymlCode)) {
			log.error("clientType为空，未找到对应的小程序配置");
			log.error("WxLoginForm:"+form.toString());
			throw new RRException("小程序异常，请联系管理员");
		}
		
		loginService.checkMobileAndCode(phoneNum, smsCode);
		
		//获取微信用户信息
		JSONObject userInfo = code2Session(jscode, ymlCode, encryptedData, iv);
		String openId = userInfo.getString("openid");
		
		//检查数据库是否已经存在该用户
		VisitorEntity visitor = visitorService.lambdaQuery().eq(VisitorEntity::getOpenId, openId).one();
		if (visitor==null) {
			visitor = visitorService.lambdaQuery().eq(VisitorEntity::getUserName, phoneNum).one();
			if (visitor==null) {
				visitor=new VisitorEntity();
				visitor.setTel(phoneNum);
				visitor.setUserName(phoneNum);
				visitor.setName(userInfo.getString("nickName"));
			}
		}
		visitor.setOpenId(openId);
        visitorService.saveOrUpdate(visitor);
        TokenEntity token = loginService.doLogin(visitor);
		return token;
	}

	/**   
	* @author xupeng
	* @date 2021-1-20 15:50:57
	* @Description:TODO 管理后台微信应用登录
	*/
	@Override
	public void sysAppLogin(Map<String, Object> params) {
		String openId = (String)params.get("openId");
		try{
			Subject subject = ShiroUtils.getSubject();
			WeChatToken token = new WeChatToken(openId);
			subject.login(token);
		}catch (UnknownAccountException e) {
			throw new RRException(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			throw new RRException("账号或密码不正确");
		}catch (LockedAccountException e) {
			throw new RRException("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			e.printStackTrace();
			throw new RRException("账户验证失败");
		}
		
	}

	/**   
	* @author xupeng
	* @date 2021-1-20 15:50:57
	* @Description:TODO 管理后台小程序端密码登录并绑定微信
	*/
	@Override
	public void sysAppLoginByPassword(String username, String password, String jscode, String ymlCode,
                                      String encryptedData, String iv) {
		try{
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
			JSONObject code2Session = this.code2Session(jscode,ymlCode,encryptedData,iv);
			SysUserEntity user = new SysUserEntity();
			user.setUserId(ShiroUtils.getUserId());
			user.setOpenId(code2Session.getString("openid"));
			sysUserService.updateById(user);
			log.info(ShiroUtils.getUserEntity().getUsername()+"-----微信绑定成功");
		}catch (UnknownAccountException e) {
			throw new RRException(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			throw new RRException("账号或密码不正确");
		}catch (LockedAccountException e) {
			throw new RRException("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			e.printStackTrace();
			throw new RRException("账户验证失败");
		}
		
	}

	/**   
	* @author xupeng
	* @date 2021-1-20 16:42:45
	* @Description:TODO 返回数据库中的openId字段
	*/
	@Override
	public String getDbField() {
		return "open_id";
	}
	
	

}
