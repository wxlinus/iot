/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 
 */

package io.iot.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/*
	* 所有用户列表(增加角色输出)
	* @author wumin
	* @date 2020/5/4
	* @return
	*/
	PageUtils listAddRole(Map<String, Object> params);

	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);

	PageUtils queryPageWithNoDataFilter(Map<String, Object> params);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);
	
	List<Map<String, Object>> getUserTreeData(List<Map<String, Object>> userTree);
	/**
	 * @author xupeng
	 * @date 2020年4月25日 下午4:02:00
	 * @Description:获取用户信息
	 * @return SysUserEntity
	 * @throws
	 */
	SysUserEntity info(Long userId);
	
	/**
	 * 小程序获取用户
	 */
	PageUtils wxqueryPage(Map<String, Object> params);

	Map<String, Object> wxGetUser(Map<String, Object> params);

	/**   
	 * @author xupeng
	 * @date 2020-7-29 17:27:41
	 * @Description:TODO 根据角色查询用户列表
	 * @param userIds
	 * @return List<SysUserEntity>
	 * @throws
	*/
	List<SysUserEntity> listUserByRoleIds(Long[] userIds);

	/**   
	 * @author xupeng
	 * @date 2020-7-29 17:38:55
	 * @Description:TODO
	 * @param params
	 * @return Object
	 * @throws
	*/
	Map<String, Object> wxGetUserByRoles(Map<String, Object> params);
}
