package io.iot.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xupeng
 *
 */
public class FtpUtil {
	
	private static FtpConfig config;
	
	static {
		config = SpringContextUtils.getBean("ftpConfig",FtpConfig.class);
	}

	/**
	* @author xupeng
	* @date 2021-1-7 13:45:12
	* @Description:TODO ftp上传文件
	* @param file
	* @param remotePath 远程路径
	* @param fileName 文件名
	* @return boolean
	* @throws
	 */
	public static boolean ftpUpload(MultipartFile file, String remotePath, String fileName) {
		try(Ftp ftp = getFtp(); InputStream is = file.getInputStream()) {
			return ftp.upload(remotePath, fileName, is);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	* @author xupeng
	* @date 2021-1-8 16:50:00
	* @Description:TODO ftp上传文件
	* @param is 文件流
	* @param remotePath 远程路径
	* @param fileName 文件名
	* @return boolean
	* @throws
	 */
	public static boolean ftpUpload(InputStream is, String remotePath, String fileName) {
		try(Ftp ftp = getFtp()) {
			return ftp.upload(remotePath, fileName, is);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	* @author xupeng
	* @date 2021-1-11 16:49:39
	* @Description:TODO ftp上传文件
	* @param file
	* @param remotePath 远程路径
	* @param fileName 文件名
	* @return boolean
	* @throws
	 */
	public static boolean ftpUpload(File file, String remotePath) {
		try(Ftp ftp = getFtp()) {
			return ftp.upload(remotePath, file);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	* @author xupeng
	* @date 2021-1-13 9:49:13
	* @Description:TODO ftp删除指定目录下的指定文件
	* @param remotePath 文件路径
	* @return boolean 是否存在
	* @throws
	 */
	public static boolean ftpDelFile(String remotePath) {
		try(Ftp ftp = getFtp()) {
			return ftp.delFile(remotePath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	* @author xupeng
	* @date 2021-1-13 9:56:39
	* @Description:TODO 获取ftp连接
	* @return Ftp
	* @throws
	 */
	public static Ftp getFtp() {
		System.err.println("ftp上传");
		Ftp ftp = new Ftp(config,null);
		ftp.init();
		return ftp;
	}
	
	public static void main(String[] args) {
		File file = FileUtil.file("E:\\xupeng\\AdminLTE-2.3.8 (1).zip");
		FtpUtil.ftpUpload(file, "test/");
	}
}
