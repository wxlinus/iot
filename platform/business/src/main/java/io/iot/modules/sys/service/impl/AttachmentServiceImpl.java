package io.iot.modules.sys.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.Constant.AttachmentType;
import io.renren.common.utils.FtpUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.AttachmentDao;
import io.renren.modules.sys.entity.AttachmentEntity;
import io.renren.modules.sys.service.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;


@Service("sysAttachmentService")
public class AttachmentServiceImpl extends ServiceImpl<AttachmentDao, AttachmentEntity> implements AttachmentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttachmentEntity> page = this.page(
                new Query<AttachmentEntity>().getPage(params),
                new QueryWrapper<AttachmentEntity>()
        );

        return new PageUtils(page);
    }

	/**   
	* @author xupeng
	* @date 2021-4-10 16:02:10
	* @Description:TODO 附件上传
	*/
	@Override
	public AttachmentEntity fileUpload(MultipartFile file, Long bizId, AttachmentType bizType) {
		//文件后缀
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		//以uuid为文件名
		String uuid = IdUtil.fastSimpleUUID();
		String filePath = bizType.getValue()+"/"+DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN
				+"/")+uuid+suffix;
		AttachmentEntity attachment = new AttachmentEntity();
		String remotePath = bizType.getValue()
				+"/"+DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN
						+"/");
		FtpUtil.ftpUpload(file, remotePath, uuid+suffix);
		attachment.setAttachmentType(file.getContentType());
		attachment.setSize(file.getSize());
		attachment.setUrl(filePath);
		attachment.setName(uuid+suffix);
		attachment.setOriginalName(file.getOriginalFilename());
		attachment.setBizType(bizType.getValue());
		if (bizId!=null) {
			attachment.setBizId(bizId);
		}
		this.save(attachment);
		return attachment;
	}

	/**   
	* @author xupeng
	* @date 2021-4-13 14:10:14
	* @Description:TODO 附件上传返回url
	*/
	@Override
	public String fileUpload(MultipartFile file, AttachmentType bizType) {
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String uuid = IdUtil.fastSimpleUUID();
		String remotePath = bizType.getValue()
				+"/"+DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN
				+"/");
		FtpUtil.ftpUpload(file, remotePath, uuid+suffix);
		return remotePath+uuid+suffix;
	}

	
}
