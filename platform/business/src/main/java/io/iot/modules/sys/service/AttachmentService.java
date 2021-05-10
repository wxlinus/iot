package io.iot.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.Constant.AttachmentType;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.AttachmentEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 附件表
 *
 * @author xupeng
 * @email ${email}
 * @date 2021-04-10 13:59:15
 */
public interface AttachmentService extends IService<AttachmentEntity> {

    PageUtils queryPage(Map<String, Object> params);

	/**   
	* @author xupeng
	* @date 2021-4-10 16:01:30
	* @Description:TODO 附件上传
	* @param file
	* @param long1
	* @param bizType
	* @return AttachmentEntity
	* @throws
	*/
	AttachmentEntity fileUpload(MultipartFile file, Long bizId, AttachmentType bizType);

	/**   
	* @author xupeng
	* @date 2021-4-13 14:09:26
	* @Description:TODO 附件上传返回url
	* @param file
	* @return String
	* @throws
	*/
	String fileUpload(MultipartFile file, AttachmentType bizType);
}

