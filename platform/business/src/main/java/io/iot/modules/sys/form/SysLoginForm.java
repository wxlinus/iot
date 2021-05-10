package io.iot.modules.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xupeng
 *
 */
@Data
@ApiModel(value = "登录表单")
public class SysLoginForm {

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名",required = true)
	private String username;
	
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码",required = true)
	private String password;
}
