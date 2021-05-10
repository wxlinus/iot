/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 */

package io.iot.modules.sys.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.annotation.SysLog;
import io.renren.common.utils.Constant;
import io.renren.common.utils.FtpUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.Assert;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.entity.SysUserRoleEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysRoleService;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysRoleService sysRoleService;



    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/listAll")
    public R listAll(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPageWithNoDataFilter(params);

        return R.ok().put("page", page);
    }
    
    @RequestMapping("findUsers")
    public R findUsers(@RequestBody Map<String, Object> params) {
    	List<SysUserEntity> list = sysUserService.list();
    	return R.ok().put("data", list);
	}
    
    @RequestMapping("/listUserByRoleIds")
    public R listUserByRoleIds(@RequestBody Long[] userIds) {
    	List<SysUserEntity> list = sysUserService.listUserByRoleIds(userIds);
    	return R.ok().put("data", list);
	}

    /**
     * 所有用户列表(增加角色输出)
     */
    @RequestMapping("/listAddRole")
    public R listAddRole(@RequestParam Map<String, Object> params){
        PageUtils page = sysUserService.listAddRole(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        SysUserEntity user = getUser();
        SysDeptEntity deptEntity = sysDeptService.getById(getDeptId());
        user.setDeptName(deptEntity.getName());
        return R.ok().put("user", user);
    }

    /**
     * @author xupeng
     * @date 2019年8月24日 上午8:50:16
     * @Description:从数据库获取登录用户信息
     */
    @RequestMapping("/currenUser")
    public R currenUser() {
    	SysUserEntity user = sysUserService.info(getUserId());
        return R.ok().put("user", user);
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = ShiroUtils.sha256(password, getUser().getSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    public R info(@PathVariable("userId") Long userId) {
//        SysUserEntity user = sysUserService.getById(userId);
        SysUserEntity user = sysUserService.info(userId);
        //获取用户所属部门
        SysDeptEntity deptEntity = sysDeptService.getById(user.getDeptId());
        user.setDeptName(deptEntity.getName());
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        sysUserService.saveUser(user);

        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        sysUserService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }
        List<SysUserRoleEntity> userRoleEntities = sysUserRoleService.list(new QueryWrapper<SysUserRoleEntity>()
                .in("user_id", Arrays.asList(userIds)));
        sysUserService.removeByIds(Arrays.asList(userIds));
        //批量删除用户和角色关系
        sysUserRoleService.removeByIds(userRoleEntities.stream()
                .map(SysUserRoleEntity::getId).collect(Collectors.toList()));

        return R.ok();
    }

    @RequestMapping("/updateUserDetails")
    @ResponseBody
    public R updateUserDetails(@RequestBody SysUserEntity userEntity) {
        userEntity.setUserId(ShiroUtils.getUserId());
        sysUserService.updateById(userEntity);
        return R.ok();
    }


    @RequestMapping("/getUserTree")
    @ResponseBody
    public R getUserTree() {
        List<SysDeptEntity> list = sysDeptService.list(new QueryWrapper<SysDeptEntity>().eq("parent_id", 0));
        List<Map<String, Object>> userTree = new ArrayList<>();
        for (SysDeptEntity dept : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", dept.getName());
            map.put("id", dept.getDeptId());
            map.put("href", "isDept");
            userTree.add(map);
        }
        sysUserService.getUserTreeData(userTree);
        return R.ok().put("list", userTree);
    }

    /**
     * 小程序: 所有用户列表
     */
    @RequestMapping("/wxList")
    @RequiresPermissions("sys:user:list")
    public R wxList(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.wxqueryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 小程序: 用户选择页
     */
    @RequestMapping("/wxGetUser")
    public R wxGetUser(@RequestParam Map<String, Object> params) {

        return R.ok().put("dataMap", sysUserService.wxGetUser(params));
    }
    
    /**
     * 小程序: 根据角色查询用户
     */
    @RequestMapping("/wxGetUserByRoles")
    public R wxGetUserByRoles(@RequestParam Map<String, Object> params) {

        return R.ok().put("dataMap", sysUserService.wxGetUserByRoles(params));
    }

    /**
     * 小程序: 根据ids获取用户列表
     */
    @RequestMapping("/wxGetUserByIds")
    public R wxGetUserByIds(@RequestBody String[] ids) {
        List<SysUserEntity> list = sysUserService.list(
                new QueryWrapper<SysUserEntity>()
                        .in("user_id", Arrays.asList(ids))
        );
        return R.ok().put("users", list);
    }


    /**
     * 上传签名图片
     */
    @RequestMapping("/uploadSignatureImg")
    public R uploadSignatureImg(@RequestParam("file") MultipartFile multipartFile) {
        String uuid = IdUtil.fastSimpleUUID();

        String remotePath = Constant.AttachmentType.SIGNATRUE.getValue()+ File.separator;
        FtpUtil.ftpUpload(multipartFile, remotePath, uuid+".png");
        return R.ok().put("path", remotePath + uuid + ".png");
    }
}
