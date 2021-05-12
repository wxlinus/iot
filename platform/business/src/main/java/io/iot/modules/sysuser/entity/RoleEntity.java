package io.iot.modules.sysuser.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 * 
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:45
 */
@Data
@TableName("sys_role")
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色id
	 */
	@TableId
	@ApiModelProperty(value = "角色id")
private Long id;
	/**
	 * 角色
	 */
	@ApiModelProperty(value = "角色")
private String role;

}
