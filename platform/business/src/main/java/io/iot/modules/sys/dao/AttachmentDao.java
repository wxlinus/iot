package io.iot.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.sys.entity.AttachmentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 附件表
 * 
 * @author xupeng
 * @email ${email}
 * @date 2021-04-10 13:59:15
 */
@Mapper
public interface AttachmentDao extends BaseMapper<AttachmentEntity> {
	
}
