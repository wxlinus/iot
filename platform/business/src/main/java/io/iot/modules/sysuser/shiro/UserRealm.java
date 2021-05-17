package io.iot.modules.sysuser.shiro;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.iot.common.utils.Constant;
import io.iot.modules.sysuser.entity.RoleEntity;
import io.iot.modules.sysuser.entity.UserEntity;
import io.iot.modules.sysuser.service.RoleService;
import io.iot.modules.sysuser.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 获取身份验证信息
     * Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("登录认证:host"+token);
        // 从数据库获取对应用户名密码的用户
//        String password = userDao.getPassword(token.getUsername());
        String password = userService.getOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, token.getUsername()))
                .getPassword();
        if (null == password) {
            throw new AccountException("用户名不正确");
        } else if (!password.equals(new String((char[]) token.getCredentials()))) {
            throw new AccountException("密码不正确");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        logger.info("权限认证:"+username);
        //获得该用户角色
//        String role = userDao.getRole(username);
        String roleId = userService.getOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, username))
                .getRoleId();
        Set<String> set = new HashSet<>();
        //管理员获得全部角色
        if (Constant.ADMIN.equals(roleId)) {
            List<RoleEntity> roleEntityList = roleService.list();
            for (RoleEntity roletemp : roleEntityList) {
                set.add(roletemp.getRole());
            }
            info.setRoles(set);
        } else {
            String role = roleService.getOne(new LambdaQueryWrapper<RoleEntity>().eq(RoleEntity::getId, roleId)).getRole();
            //需要将 role 封装到 Set 作为 info.setRoles() 的参数
            set.add(role);
            //设置该用户拥有的角色
            info.setRoles(set);
        }
        return info;
    }
}
