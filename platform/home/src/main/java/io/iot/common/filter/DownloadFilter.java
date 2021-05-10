package io.iot.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 下载文件时响应头添加字段告诉浏览器下载文件而不是打开文件
 *
 * @author libaogang
 * @date 2019-10-15 14:57:09
 */
public class DownloadFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=");

        chain.doFilter(request, httpServletResponse);


    }

    @Override
    public void destroy() {
    }

}