package io.iot.modules.sysuser.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.iot.common.exception.RRException;
import io.iot.common.validator.ValidatorUtils;
import io.iot.modules.sysuser.dao.UserDao;
import io.iot.modules.sysuser.entity.RoleEntity;
import io.iot.modules.sysuser.service.RoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.iot.modules.sysuser.entity.UserEntity;
import io.iot.modules.sysuser.service.UserService;
import io.iot.common.utils.PageUtils;
import io.iot.common.utils.R;

/**
 * 系统用户
 *
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:44
 */
@RestController
@RequestMapping("sysuser/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user){
        userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user){
        ValidatorUtils.validateEntity(user);
        userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/login")
    public R login(String username, String password) {
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登陆
        subject.login(token);
        //根据权限，指定返回数据
        String role = userService.getRole(username);
        System.out.println(role);
        List<RoleEntity> roleEntityList=roleService.list();
        if(roleEntityList==null) throw new RRException("权限数据为空");
        for(RoleEntity roletemp:roleEntityList){
            if(roletemp.getRole().equals(role))
                return R.ok("欢迎登陆");
            if (roletemp.getRole().equals(role)) {
                return R.ok("欢迎来到管理员页面");
            }
        }
        return R.error("权限错误！").put("role",role);
    }
    @RequestMapping(value = "/logout")
    public R logout() {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        return R.ok("成功注销！");
    }
}
