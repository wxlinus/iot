/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 
 */

package io.iot.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**   
	* @author xupeng
	* @date 2019年11月28日 上午10:33:51
	* @Description:根据openid查询用户
	*/
	SysUserEntity getByOpenId(String principal);

	/*
	* 查询用户信息包括部门,角色
	* @author wumin
	* @date 2020/5/4 10:10
	* @return
	*/
	List<SysUserEntity> getuserInfo(Map<String, Object> params);

	/**   
	 * @author xupeng
	 * @date 2020-7-29 17:31:33
	 * @Description:TODO 根据角色查询用户列表
	 * @param userIds
	 * @return List<SysUserEntity>
	 * @throws
	*/
	List<SysUserEntity> listUserByRoleIds(Long[] userIds);


}
