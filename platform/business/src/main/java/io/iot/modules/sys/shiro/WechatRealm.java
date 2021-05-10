package io.iot.modules.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.Constant;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.dao.SysMenuDao;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysMenuEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.MiniProgramService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author xupeng
 *
 */
@Component
public class WechatRealm extends AuthorizingRealm {
	
	@Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysDeptDao sysDeptDao;
    @Lazy
    @Autowired
    private MiniProgramService programService;
    /**
     * 微信用户id模式
     */
    @Value("${wx.model}")
    private String wxModel;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
		Long userId = user.getUserId();
		
		List<String> permsList;
		
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
			permsList = new ArrayList<>(menuList.size());
			for(SysMenuEntity menu : menuList){
				permsList.add(menu.getPerms());
			}
		}else{
			permsList = sysUserDao.queryAllPerms(userId);
		}

		//用户权限列表
		Set<String> permsSet = new HashSet<>();
		for(String perms : permsList){
			if(StringUtils.isBlank(perms)){
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		WeChatToken token = (WeChatToken) authcToken;
		
		SysUserEntity user = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq(programService.getDbField(), token.getPrincipal()));
		//账号不存在
		if(user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		SysDeptEntity dept = sysDeptDao.selectOne(
				new QueryWrapper<SysDeptEntity>().eq("dept_id", user.getDeptId()));
		user.setDeptName(dept.getName());
        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,null,this.getName());
//        setAttribute("currentUserName", user.getUsername());
        return authenticationInfo;
	}
	
	@Override
    public boolean supports(AuthenticationToken token){
        return token!=null&&token instanceof WeChatToken;
    }
    /**
     * 认证密码匹配调用方法
     */
	@Override
	protected void assertCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) throws AuthenticationException {
		return;
	}

}
