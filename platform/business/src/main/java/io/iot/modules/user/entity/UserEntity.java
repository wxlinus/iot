package io.iot.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * 
 * @author weixiang
 * @email ${email}
 * @date 2021-05-11 15:09:48
 */
@Data
@TableName("tk_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId
	@ApiModelProperty(value = "用户id")
private Long id;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
private String name;
	/**
	 * 年龄
	 */
	@ApiModelProperty(value = "年龄")
private Integer age;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
private String tel;
	/**
	 * 性别 0--男 1--女 2--未知
	 */
	@ApiModelProperty(value = "性别 0--男 1--女 2--未知")
private Integer sex;

}
