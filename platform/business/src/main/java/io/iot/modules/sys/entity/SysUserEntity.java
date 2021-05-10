/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 */

package io.iot.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user")
@ToString
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空", groups = AddGroup.class)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
//    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
//    @Email(message = "邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态 0：禁用 1：正常
	 */
	private Integer status;

	/**
	 * 角色ID列表
	 */
	@TableField(exist = false)
	private List<Long> roleIdList;

	/**
	 * 角色名称
	 */
	@TableField(exist = false)
	private String roleName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 部门ID
	 */
	@NotNull(message = "部门不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private Long deptId;

	/**
	 * 部门名称
	 */
	@TableField(exist = false)
	private String deptName;
	/**
	 * 姓名
	 */
	private String realname;
	/**
	 * 职位
	 */
	private String duty;
	/**
	 * 1男，2女
	 */
	private Integer gender;
	/**
	 * 生日
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date birthday;
	/**
	 * 身份证号
	 */
	private String idcard;
	/**
	 * 家庭住址
	 */
	private String address;

	@TableField(exist = false)
	private BigDecimal totalCost;
	/**
	 * 微信用户openid
	 */
	private String openId;
	/**
	 * 微信号
	 */
	private String wechatNum;

	/**
	 * 签名图片
	 */
	private String signatureImg;

	/**
	 * 用户编号
	 */
	private String number;

	/**
	 * 是否加入考勤：0不加入1加入
	 */
	private Integer isJoinAttendance;

	@TableField(exist = false)
	private Float monthStarScore;

	@TableField(exist = false)
	private Float monthScore;
	@TableField(exist = false)
	private String roleNames;

	private String unionId;
}
