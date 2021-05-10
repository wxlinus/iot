package io.iot.common.filter;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author xupeng
 *
 */
public class RequestParameterFilter implements Filter {

	@Value("${project.baseName}")
	private String baseName;
	
	/**   
	* @author xupeng
	* @date 2021-1-6 9:00:48
	* @Description:TODO 请求参数处理
	*/
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute("baseName", baseName);
		chain.doFilter(request, response);
	}

}
