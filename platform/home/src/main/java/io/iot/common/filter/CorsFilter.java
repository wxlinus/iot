package io.iot.common.filter;

import io.renren.common.utils.ParameterRequestWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xupeng
 *
 */
public class CorsFilter implements Filter {

	/**   
	* @author xupeng
	* @date 2020-10-30 13:32:32
	* @Description:TODO 跨域过滤
	*/
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String origin = request.getHeader("Origin");
        if(origin == null) {
            origin = request.getHeader("Referer");
        }
        // 允许哪些Origin发起跨域请求
        response.setHeader( "Access-Control-Allow-Origin", origin );
        // 允许请求的方法
        response.setHeader( "Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT" );
        //多少秒内,不需要再发送预检验请求，可以缓存该结果
        response.setHeader( "Access-Control-Max-Age", "3600" );
        // 表明它允许跨域请求包含xxx头
        response.setHeader( "Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //是否允许浏览器携带用户身份信息（cookie）
        response.setHeader( "Access-Control-Allow-Credentials", "true" );
        
	    if (request.getMethod().equals( "OPTIONS" )) {
	        response.setStatus(HttpStatus.OK.value());
	        return;
	    }
        
        if (StringUtils.isBlank(request.getParameter("JSESSIONID"))) {
        	Map paramter = new HashMap();
        	paramter.put("JSESSIONID", request.getHeader("JSESSIONID"));
        	ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request,paramter);
        	chain.doFilter(wrapper, response);
		}else {
			chain.doFilter(request, response);
		}
        
//        String sessionid=request.getHeader("JSESSIONID");
//        chain.doFilter(req, res);  

	}

}
