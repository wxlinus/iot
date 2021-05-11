package io.iot.common.utils;

/**
 * @author xupeng
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileUtil {

	/**
	 * 创建目录及文件,如果目录不存在则先创建目录在创建文件
	 * @author xupeng
	 * @date 2018-05-03
	 */
	public static File createDirAndFile(String path){
		String absolutePath = cn.hutool.core.io.FileUtil.getAbsolutePath(path);
		File file = new File(absolutePath);
		File fileParent = file.getParentFile();
		if(!fileParent.exists()){  
		    fileParent.mkdirs();  
		}  
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public static File createDirAndFile(File file){
		File fileParent = file.getParentFile();
		if(!fileParent.exists()){  
			fileParent.mkdirs();  
		}  
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 *@Description 下载网络文件
	 *@author xupeng
	 *@date 2019年2月21日 上午11:24:37
	 */
	public static void downloadNet(String path) throws MalformedURLException {
		String[] names = {"青云","迷局","宏愿","惊变","入门"};
		
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        try {
        	URL url = new URL(path);
        	
        	URLConnection conn = url.openConnection();
        	InputStream inStream = conn.getInputStream();
        	FileOutputStream fs = new FileOutputStream("c:/00.mp3");
        	
        	byte[] buffer = new byte[1204];
        	int length;
        	while ((byteread = inStream.read(buffer)) != -1) {
        		bytesum += byteread;
        		System.out.println(bytesum);
        		fs.write(buffer, 0, byteread);
        	}
        	fs.close();
        	inStream.close();
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
	}
}