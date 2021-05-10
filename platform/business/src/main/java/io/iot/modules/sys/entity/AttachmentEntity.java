package io.iot.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件表
 * 
 * @author xupeng
 * @email ${email}
 * @date 2021-04-10 13:59:15
 */
@Data
@TableName("t_attachment")
public class AttachmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Long id;
	/**
	 * 业务表id
	 */
	@ApiModelProperty(value = "业务表id")
	private Long bizId;
	/**
	 * 业务类型
	 */
	@ApiModelProperty(value = "业务类型")
	private String bizType;
	/**
	 * 附件名称
	 */
	@ApiModelProperty(value = "附件名称")
	private String name;
	/**
	 * 附件原始名称
	 */
	@ApiModelProperty(value = "附件原始名称")
	private String originalName;
	/**
	 * 附件地址
	 */
	@ApiModelProperty(value = "附件地址")
	private String url;
	/**
	 * 附件类型
	 */
	@ApiModelProperty(value = "附件类型")
	private String attachmentType;
	/**
	 * 附件大小（字节）
	 */
	@ApiModelProperty(value = "附件大小（字节）")
	private Long size;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**
	 * 0未删除-1已删除
	 */
	@ApiModelProperty(value = "0未删除-1已删除")
	private Integer delFlag;

}
