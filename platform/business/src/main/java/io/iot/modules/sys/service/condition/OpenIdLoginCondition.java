package io.iot.modules.sys.service.condition;

import io.renren.modules.sys.constant.SysConstant;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author xupeng
 *
 */
public class OpenIdLoginCondition implements Condition {

	/**   
	* @author xupeng
	* @date 2021-1-20 14:47:36
	* @Description:TODO 微信OpenId登录条件
	*/
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return SysConstant.WX_MODEL_OPENID.equals(context.getEnvironment().getProperty("wx.model"));
	}

}
