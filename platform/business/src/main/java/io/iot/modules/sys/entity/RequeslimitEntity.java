package io.iot.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑白名单表
 * 
 * @author xupeng
 * @email ${email}
 * @date 2020-03-25 08:59:46
 */
@Data
@TableName("t_request_limit")
public class RequeslimitEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * ip地址
	 */
	private String ip;
	/**
	 * ip类型：0黑名单1白名单
	 */
	private Integer type;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 0未删除1已删除
	 */
//	@TableLogic
	private Integer isDelete;

}
