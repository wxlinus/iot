package io.iot.modules.sys.controller;

import cn.hutool.http.HttpUtil;
import io.renren.common.exception.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xupeng
 * 文件服务
 */
@RestController
@Slf4j
public class DownloadController {

	@Value("${project.file.server-url}")
	private String fileServerUrl;
	
	@RequestMapping("/download/**")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		String requestURI = request.getRequestURI();
		System.err.println(requestURI);
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			long size = HttpUtil.download(fileServerUrl+requestURI, outputStream, false);
			log.info("附件大小："+size);
		} catch (IOException e) {
//			e.printStackTrace();
			log.error("附件下载失败");
			log.error("project.file.server-url: "+fileServerUrl+requestURI);
			throw new RRException("附件下载失败");
		} 
	}
}
