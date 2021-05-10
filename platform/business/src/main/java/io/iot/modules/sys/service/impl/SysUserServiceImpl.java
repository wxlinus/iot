/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 */

package io.iot.modules.sys.service.impl;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.annotation.DataFilter;
import io.renren.common.exception.RRException;
import io.renren.common.utils.ChineseCharacterUtil;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.constant.SysConstant;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysRoleEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.entity.SysUserRoleEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysRoleService;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysDeptService sysDeptService;
    @Resource
    private SysUserDao sysUserDao;

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        String realname = (String) params.get("realname");
        String deptId = (String) params.get("deptId");
        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .like(StringUtils.isNotBlank(realname), "realname", realname)
                        .eq(StringUtils.isNotBlank(deptId), "dept_id", deptId)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );

        for (SysUserEntity sysUserEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            sysUserEntity.setDeptName(sysDeptEntity.getName());
        }

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageWithNoDataFilter(Map<String, Object> params) {
        String username = (String) params.get("username");
        String realname = (String) params.get("realname");
        String deptId = (String) params.get("deptId");
        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .like(StringUtils.isNotBlank(realname), "realname", realname)
                        .eq(StringUtils.isNotBlank(deptId), "dept_id", deptId)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );

        for (SysUserEntity sysUserEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            sysUserEntity.setDeptName(sysDeptEntity.getName());
        }

        return new PageUtils(page);
    }

    @Override
    public PageUtils listAddRole(Map<String, Object> params) {

        IPage<SysUserEntity> page = new Query<SysUserEntity>().getPage(params);
        List<SysUserEntity> list = sysUserDao.getuserInfo(params);

        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserEntity user) {
        if (StrUtil.isNotBlank(user.getNumber())) {
            SysUserEntity one = this.lambdaQuery().eq(SysUserEntity::getNumber, user.getNumber()).one();
            if (one != null) {
                throw new RRException("编号为" + user.getNumber() + "的员工已存在");
            }
        }
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        this.save(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserEntity user) {
        if (ShiroUtils.getUserId() != Constant.SUPER_ADMIN && user.getUserId() == Constant.SUPER_ADMIN) {
            throw new RRException("无法修改超级管理员信息");
        }
        if (StrUtil.isNotBlank(user.getNumber())) {
            SysUserEntity userEntity = this.getById(user.getUserId());
            SysUserEntity one = this.lambdaQuery().eq(SysUserEntity::getNumber, user.getNumber()).one();

            if (one != null && !one.getNumber().equals(userEntity.getNumber())) {
                throw new RRException("编号为" + user.getNumber() + "的员工已存在");
            }
        }
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            SysUserEntity userEntity = this.getById(user.getUserId());

            user.setPassword(ShiroUtils.sha256(user.getPassword(), userEntity.getSalt()));
        }
        this.updateById(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }


    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

    /**
     * 用户树
     */
    @Override
    public List<Map<String, Object>> getUserTreeData(List<Map<String, Object>> userTree) {
        List<Map<String, Object>> treeData = new ArrayList<>();
        for (Map<String, Object> tree : userTree) {
            List<SysDeptEntity> subDeptList = sysDeptService.list(
                    new QueryWrapper<SysDeptEntity>().eq("parent_id", tree.get("id")));
            List<SysUserEntity> userList = this.list(
                    new QueryWrapper<SysUserEntity>().eq("dept_id", tree.get("id")));
            List<Map<String, Object>> children = new ArrayList<>();
            if (userList.size() > 0) {
                for (SysUserEntity user : userList) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("title", user.getRealname());
                    userMap.put("id", user.getUserId());
                    userMap.put("href", "isUser");
                    children.add(userMap);
                    treeData.add(userMap);
                }
                tree.put("children", children);
            }
            if (subDeptList.size() > 0) {
                for (SysDeptEntity dept : subDeptList) {
                    Map<String, Object> deptMap = new HashMap<>();
                    List<Map<String, Object>> tempdept = new ArrayList<>();
                    deptMap.put("title", dept.getName());
                    deptMap.put("id", dept.getDeptId());
                    deptMap.put("href", "isDept");
                    tempdept.add(deptMap);
                    deptMap.put("children", getUserTreeData(tempdept));
                    children.add(deptMap);
                    treeData.add(deptMap);
                }
                tree.put("children", children);
            }
        }
        return treeData;
    }

    /**
     * 小程序: 所有用户列表
     */
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils wxqueryPage(Map<String, Object> params) {
        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .and(StringUtils.isNotBlank((String) params.get("option")),
                                wrapper -> wrapper
                                        .like("username", params.get("option"))
                                        .or()
                                        .like("realname", params.get("option"))
                                        .or()
                                        .eq("mobile", params.get("option")))
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );

        for (SysUserEntity sysUserEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            sysUserEntity.setDeptName(sysDeptEntity.getName());
        }

        return new PageUtils(page);
    }


    /**
     * 小程序: 用户选择页
     */
    @Override
    public Map<String, Object> wxGetUser(Map<String, Object> params) {
        Map<String, Object> dataMap = new HashMap<>();
        List<SysUserEntity> list = this.list(new QueryWrapper<SysUserEntity>()
                .and(StringUtils.isNotBlank((String) params.get("userInfo")), wrapper -> wrapper
                        .like("username", params.get("userInfo"))
                        .or()
                        .like("realname", params.get("userInfo"))
                        .or()
                        .eq("mobile", params.get("userInfo"))
                )
        );
        Map<String, List<SysUserEntity>> collect = list.parallelStream().collect(Collectors.groupingBy(r -> {
                    //获取名称首字母
                    return String.valueOf(ChineseCharacterUtil.getUpperCase(r.getRealname(), false).toUpperCase().charAt(0));
                }
        ));
        List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
        List<String> indexList = new ArrayList<String>();
        for (Entry<String, List<SysUserEntity>> entry : collect.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", entry.getKey());
            map.put("users", entry.getValue());
            users.add(map);
            indexList.add(entry.getKey());
        }
        dataMap.put("users", users);
        dataMap.put("indexList", indexList);
        return dataMap;
    }

	
	@Override
	public Map<String, Object> wxGetUserByRoles(Map<String, Object> params) {
		Map<String, Object> dataMap = new HashMap<>();
        List<SysUserEntity> list = this.listUserByRoleIds(new Long[] {SysConstant.EXHIBITION_GROUP_LEADER_ROLE_ID});
        Map<String, List<SysUserEntity>> collect = list.parallelStream().collect(Collectors.groupingBy(r -> {
                    //获取名称首字母
                    return String.valueOf(ChineseCharacterUtil.getUpperCase(r.getRealname(), false).toUpperCase().charAt(0));
                }
        ));
        List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
        List<String> indexList = new ArrayList<String>();
        for (Entry<String, List<SysUserEntity>> entry : collect.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", entry.getKey());
            map.put("users", entry.getValue());
            users.add(map);
            indexList.add(entry.getKey());
        }
        dataMap.put("users", users);
        dataMap.put("indexList", indexList);
        return dataMap;
	}

	/**   
	 * @author xupeng
	 * @date 2020年4月25日 下午4:02:34
	 * @Description:获取用户信息
	 * @return
	 * @throws
	*/
	@Override
	public SysUserEntity info(Long userId) {
		SysUserEntity user = this.lambdaQuery().eq(SysUserEntity::getUserId, userId).one();
		List<SysUserRoleEntity> userRoleList = sysUserRoleService.lambdaQuery()
				.eq(SysUserRoleEntity::getUserId, userId).list();
		List<Long> roleIds = userRoleList.stream().map(SysUserRoleEntity::getRoleId)
				.collect(Collectors.toList());
		List<SysRoleEntity> roleList = sysRoleService.lambdaQuery()
				.in(SysRoleEntity::getRoleId, roleIds).list();
		List<String> roleName = roleList.stream().map(SysRoleEntity::getRoleName)
				.collect(Collectors.toList());
		String roleNameJoin = ArrayUtil.join(ArrayUtil.toArray(roleName, String.class),",");
		user.setRoleNames(roleNameJoin);
		user.setRoleIdList(roleIds);
		return user;
	}

	/**   
	 * @author xupeng
	 * @date 2020-7-29 17:28:32
	 * @Description:TODO 根据角色查询用户列表
	 * @param userIds
	 * @return
	 * @throws
	*/
	@Override
	public List<SysUserEntity> listUserByRoleIds(Long[] userIds) {
		return baseMapper.listUserByRoleIds(userIds);
	}
    
    
}
