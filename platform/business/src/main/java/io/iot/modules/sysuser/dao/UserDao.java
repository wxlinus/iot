package io.iot.modules.sysuser.dao;

import io.iot.modules.sysuser.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户
 * 
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:44
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
    String getPassword(@Param(value = "userName") String userName);
    String getRole(@Param(value = "userName") String userName);
	
}
