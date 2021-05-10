package io.iot.common.interceptor;

import io.renren.common.annotation.IpFilter;
import io.renren.common.exception.RRException;
import io.renren.common.utils.IPUtils;
import io.renren.modules.sys.entity.RequeslimitEntity;
import io.renren.modules.sys.service.RequeslimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ip拦截器
 * @author xupeng
 */
@Slf4j
@Component
public class IpInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RequeslimitService requeslimitService;

	/**   
	* @author xupeng
	* @date 2020年6月4日 上午10:55:04
	* @Description:TODO 拦截并校验ip
	*/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		IpFilter annotation;
		if (handler instanceof HandlerMethod) {
			annotation = ((HandlerMethod)handler).getMethodAnnotation(IpFilter.class);
		}else {
			return true;
		}
		
		if (annotation == null) {
			return true;
		}
		
		//获取用户ip
		String userIp = IPUtils.getIpAddr(request);
		if ("0:0:0:0:0:0:0:1".equals(userIp)) {
			return true;
		}
		//查询白名单
		List<RequeslimitEntity> ipList = requeslimitService.lambdaQuery()
				.eq(RequeslimitEntity::getIp, userIp)
				.eq(RequeslimitEntity::getType, 1).list();
		if (null==ipList||ipList.size()==0) {
			log.error("请求来源："+userIp+"无权访问该资源");
			throw new RRException("无权访问该资源");
		}
		return true;
	}
	
	
}
