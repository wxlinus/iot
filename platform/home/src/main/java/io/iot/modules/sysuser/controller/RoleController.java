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

import io.iot.modules.sysuser.entity.RoleEntity;
import io.iot.modules.sysuser.service.RoleService;
import io.iot.common.utils.PageUtils;
import io.iot.common.utils.R;



/**
 * 角色表
 *
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:45
 */
@RestController
@RequestMapping("sysuser/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = roleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RoleEntity role = roleService.getById(id);

        return R.ok().put("role", role);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RoleEntity role){
        roleService.save(role);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RoleEntity role){
        ValidatorUtils.validateEntity(role);
        roleService.updateById(role);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        roleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
