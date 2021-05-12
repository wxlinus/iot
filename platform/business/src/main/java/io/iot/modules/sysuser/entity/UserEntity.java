package io.iot.modules.sysuser.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 *
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:44
 */
@Data
@TableName("sys_user")
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
    private String username;
    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;
    /**
     * 盐
     */
    @ApiModelProperty(value = "盐")
    private String salt;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String realname;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String image;
    /**
     * 性别 0--男 1--女 2--未知
     */
    @ApiModelProperty(value = "性别 0--男 1--女 2--未知")
    private Integer gender;
    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "生日")
    private Date birthday;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String tel;
    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证")
    private String idcard;
    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识")
    private Integer uuid;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 角色
     */
    @ApiModelProperty(value = "角色")
    private String roleId;

}
