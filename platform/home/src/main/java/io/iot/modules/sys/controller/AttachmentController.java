package io.iot.modules.sys.controller;

import io.renren.common.exception.RRException;
import io.renren.common.utils.Constant.AttachmentType;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.entity.AttachmentEntity;
import io.renren.modules.sys.service.AttachmentService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;


/**
 * 附件表
 *
 * @author xupeng
 * @email ${email}
 * @date 2021-04-10 13:59:15
 */
@RestController
@RequestMapping("sys/attachment")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:attachment:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attachmentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:attachment:info")
    public R info(@PathVariable("id") Long id){
        AttachmentEntity attachment = attachmentService.getById(id);

        return R.ok().put("attachment", attachment);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:attachment:save")
    public R save(@RequestBody AttachmentEntity attachment){
        attachmentService.save(attachment);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:attachment:update")
    public R update(@RequestBody AttachmentEntity attachment){
        ValidatorUtils.validateEntity(attachment);
        attachmentService.updateById(attachment);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:attachment:delete")
    public R delete(@RequestBody Long[] ids){
        attachmentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    
    /**
    * @author xupeng
    * @date 2021-4-10 15:50:36
    * @Description:TODO 附件上传
    * @return R
    * @throws
     */
    @PostMapping("/fileUpload")
    public R fileUpload(@RequestParam("file") MultipartFile file
    		,@RequestParam(required=false,value="bizId") String bizId
    		,@RequestParam String bizType) {
    	AttachmentEntity attachment = attachmentService.fileUpload(file
    			,StringUtils.isNotBlank(bizId) ? Long.valueOf(bizId) : null
				,AttachmentType.getByValue(bizType));
    	return R.ok().put("data", attachment);
    }
    
    /**
    * @author xupeng
    * @date 2021-4-14 14:12:45
    * @Description:TODO 附件上传返回url
    * @param file
    * @param bizType
    * @return R
    * @throws
     */
    @PostMapping("/fileUploadReturnUrl")
    public R fileUploadReturnUrl(@RequestParam("file") MultipartFile file, @RequestParam String bizType) {
    	AttachmentType attachmentType = AttachmentType.getByValue(bizType);
    	if (attachmentType==null) {
			throw new RRException("附件业务类型不匹配");
		}
    	String url = attachmentService.fileUpload(file,attachmentType);
    	return R.ok().put("data", url);
    }

}
