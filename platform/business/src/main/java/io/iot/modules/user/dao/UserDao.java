package io.iot.modules.user.dao;

import io.iot.modules.user.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表
 * 
 * @author weixiang
 * @email ${email}
 * @date 2021-05-11 15:09:48
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	
}
