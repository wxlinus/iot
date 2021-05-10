package io.iot.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xupeng
 *
 */
@Slf4j
public class LoginFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		String sessionId = WebUtils.toHttp(request).getSession().getId();
		if (isLoginRequest(request, response)) {
			return true;
		}else {
			Subject subject = getSubject(request, response);
            return subject.getPrincipal() != null;
		}
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest rq = WebUtils.toHttp(request);
		String clientType = rq.getHeader("client_type");
		if (StringUtils.isBlank(clientType)) {
			clientType=rq.getParameter("client_type");
		}
		if ("miniProgram".equals(clientType)||isAjax(request)) {
			timeout(response);
		}else if ("Web-Static".equals(clientType)) {
			sessionOverdue(response);
		}else {
//			rq.getRequestDispatcher("/login.html").forward(request, response);
			saveRequestAndRedirectToLogin(request, response);
//			WebUtils.toHttp(response).sendRedirect("/login.html");
		}
		return false;
	}

	private boolean isAjax(ServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
	}

	private void timeout(ServletResponse servletResponse) {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setCharacterEncoding("UTF-8");
		response.setHeader("session-status", "timeout");
		try {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "session_timeout");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sessionOverdue(ServletResponse servletResponse) {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setCharacterEncoding("UTF-8");
		response.setHeader("session-status", "timeout");
		try {
			response.sendRedirect("/sessionout");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
