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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
   * @author: weixang
   * @date: 2021/5/17 15:46
   * @description:登录方法
   * @param: 用户名，密码
   * @return: 返回登录结果
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
        List<RoleEntity> roleEntityList=roleService.list();
        if(roleEntityList==null) throw new RRException("权限数据库为空");
        for(RoleEntity roletemp:roleEntityList){
            if(roletemp.getRole().equals(role))
            {
                return R.ok("登录成功").put("role",roletemp.getRole());
            }
        }
        return R.error("权限错误！").put("role",role);
    }

    /**
     * @author: weixiang
     * @date: 2021/5/17 15:47
     * @description:注销登录
     * @param: 空
     * @return: 注销结果
     */
    @RequestMapping(value = "/logout")
    public R logout() {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        return R.ok("成功注销！");
    }
}
