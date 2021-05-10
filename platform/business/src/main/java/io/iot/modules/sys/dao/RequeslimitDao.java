package io.iot.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.sys.entity.RequeslimitEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 黑白名单表
 * 
 * @author xupeng
 * @email ${email}
 * @date 2020-03-25 08:59:46
 */
@Mapper
public interface RequeslimitDao extends BaseMapper<RequeslimitEntity> {
	
}
