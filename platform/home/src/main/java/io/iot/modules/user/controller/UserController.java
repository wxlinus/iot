package io.iot.modules.user.controller;

import java.util.Arrays;
import java.util.Map;

import io.iot.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.iot.modules.user.entity.UserEntity;
import io.iot.modules.user.service.UserService;
import io.iot.common.utils.PageUtils;
import io.iot.common.utils.R;


/**
 * 用户信息表
 *
 * @author weixiang
 * @email ${email}
 * @date 2021-05-11 15:09:48
 */
@Api(tags = "接口文档")
@RestController
@RequestMapping("user/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 列表
     */
    @ApiOperation(value = "获取用户信息", httpMethod = "GET")
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user) {
        userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user) {
        ValidatorUtils.validateEntity(user);
        userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
