/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 */

package io.iot.common.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 *
 * @author Mark sunlightcs@gmail.com
 */
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

//		String path = ((HttpServletRequest) request).getRequestURI();
//
//		if (path.startsWith("/notice/notice/save")) {   // 支持排除特定url  -- add by libaogang 2019/10/18
//			chain.doFilter(request, response);
//		}else{
//			XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
//					(HttpServletRequest) request);
//			chain.doFilter(xssRequest, response);
//		}

        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
                (HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void destroy() {
    }

}