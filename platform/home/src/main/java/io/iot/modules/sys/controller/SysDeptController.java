/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 
 */

package io.iot.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.RRException;
import io.renren.common.utils.Constant;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysRoleDeptService;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 部门管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public List<SysDeptEntity> list(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		return deptList;
	}

	/**
	 * 无权限列表
	 */
	@RequestMapping("/listAll")
	public List<SysDeptEntity> listAll(){
		return sysDeptService.queryAllList(new HashMap<String, Object>());
	}

	@RequestMapping("/queryLeafDept")
	public List<SysDeptEntity> queryLeafDept(){
		return sysDeptService.queryLeafDept();
	}
	
	/**
	 * 根据用户id获取用户权限部门id
	 */
	@RequestMapping("/getDeptList/{userId}")
	public R getDeptList(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);
		if(user==null) {
			throw new RRException("抄送人不存在！");
		}
		//部门ID列表
        Set<Long> deptIdList = new HashSet<>();
        deptIdList.add(user.getDeptId());
		//用户角色对应的部门ID列表
	    List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
	    if(roleIdList.size() > 0){
	        List<Long> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
	        deptIdList.addAll(userDeptIdList);
	    }
	    //用户子部门ID列表
	    List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
	    deptIdList.addAll(subDeptIdList);
		return R.ok().put("deptList", deptIdList);
	}
	/**
	 * 选择部门(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:dept:select")
	public R select(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		//添加一级部门
		if(getUserId() == Constant.SUPER_ADMIN){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId(0L);
			root.setName("一级部门");
			root.setParentId(-1L);
			root.setOpen(true);
			deptList.add(root);
		}

		return R.ok().put("deptList", deptList);
	}

	/**
	 * 上级部门Id(管理员则为0)
	 */
	@RequestMapping("/info")
	@RequiresPermissions("sys:dept:list")
	public R info(){
		long deptId = 0;
		if(getUserId() != Constant.SUPER_ADMIN){
			List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
			Long parentId = null;
			for(SysDeptEntity sysDeptEntity : deptList){
				if(parentId == null){
					parentId = sysDeptEntity.getParentId();
					continue;
				}

				if(parentId > sysDeptEntity.getParentId().longValue()){
					parentId = sysDeptEntity.getParentId();
				}
			}
			deptId = parentId;
		}

		return R.ok().put("deptId", deptId);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{deptId}")
	@RequiresPermissions("sys:dept:info")
	public R info(@PathVariable("deptId") Long deptId){
		SysDeptEntity dept = sysDeptService.getById(deptId);
		
		return R.ok().put("dept", dept);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:dept:save")
	public R save(@RequestBody SysDeptEntity dept){
		ValidatorUtils.validateEntity(dept, AddGroup.class);
		sysDeptService.save(dept);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:dept:update")
	public R update(@RequestBody SysDeptEntity dept){
		ValidatorUtils.validateEntity(dept, UpdateGroup.class);
		sysDeptService.updateById(dept);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:dept:delete")
	public R delete(long deptId){
		//判断是否有子部门
		List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			return R.error("请先删除子部门");
		}
		//判断部门下是否有用户
		List<SysUserEntity> userList = sysUserService.list(new QueryWrapper<SysUserEntity>()
				.eq("dept_id", deptId));
		if (userList.size()>0) {
			return R.error("该部门下有用户，不可删除");
		}
		sysDeptService.removeById(deptId);
		
		return R.ok();
	}
	
}
