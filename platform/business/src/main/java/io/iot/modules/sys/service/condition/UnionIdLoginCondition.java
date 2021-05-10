package io.iot.modules.sys.service.condition;

import io.renren.modules.sys.constant.SysConstant;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author xupeng
 *
 */
public class UnionIdLoginCondition implements Condition {

	/**   
	* @author xupeng
	* @date 2021-1-20 14:50:07
	* @Description:TODO 微信UnionId登录条件
	*/
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return SysConstant.WX_MODEL_UNIONID.equals(context.getEnvironment().getProperty("wx.model"));
	}

}
