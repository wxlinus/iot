package io.iot.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import io.renren.modules.login.entity.TokenEntity;
import io.renren.modules.login.form.WxBindMobileForm;

import java.util.Map;

/**
 * @author xupeng
 *
 */
public interface MiniProgramService {

	/**   
	* @author xupeng
	* @date 2019年12月12日 上午10:25:55
	* @Description:登录凭证校验
	*/
	JSONObject code2Session(String jscode, String ymlCode, String encryptedData, String iv);
	/**
	* @author xupeng
	* @date 2020-12-11 15:26:17
	* @Description:TODO 微信登录
	* @param jscode
	* @param ymlCode
	* @param encryptedData
	* @param iv
	* @return JSONObject
	* @throws
	 */
	TokenEntity wxLogin(String jscode, String ymlCode, String encryptedData, String iv);
	/**
	 *
	* @author xupeng
	* @date 2020-12-12 14:44:14
	* @Description:TODO 微信登录并绑定手机号
	* @param jscode
	* @param ymlCode
	* @param encryptedData
	* @param iv
	* @return TokenEntity
	* @throws
	 */
	TokenEntity wxBindMobileAndLogin(WxBindMobileForm form);
	/**
	* @author xupeng
	* @date 2021-1-20 15:45:26
	* @Description:TODO 管理后台微信应用登录
	* @param params
	* @return void
	* @throws
	 */
	void sysAppLogin(Map<String, Object> params);
	/**
	* @author xupeng
	* @date 2021-1-20 15:48:58
	* @Description:TODO 管理后台小程序端密码登录并绑定微信
	* @param username 管理后台用户名
	* @param password 管理后台密码
	* @param jscode
	* @param ymlCode 指定yml配置
	* @param encryptedData
	* @param iv
	* @return void
	* @throws
	 */
	void sysAppLoginByPassword(String username, String password, String jscode, String ymlCode, String encryptedData, String iv);
	/**
	* @author xupeng
	* @date 2021-1-20 16:40:22
	* @Description:TODO 获取微信用户id在数据库对应的字段（open_id,union_id）
	* @return String
	* @throws
	 */
	String getDbField();
}
