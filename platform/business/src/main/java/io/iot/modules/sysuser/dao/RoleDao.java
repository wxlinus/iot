package io.iot.modules.sysuser.dao;

import io.iot.modules.sysuser.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色表
 * 
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:45
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {
	
}
