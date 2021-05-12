package io.iot.modules.sysuser.controller;

import java.util.Arrays;
import java.util.Map;

import io.iot.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

}
