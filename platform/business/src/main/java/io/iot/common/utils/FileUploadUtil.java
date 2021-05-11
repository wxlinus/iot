package io.iot.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * 文件上传
 *
 * @author wj
 * @date 2019/9/15 11:17
 */
public class FileUploadUtil {
    private static final String UPLOADFILE_TYPE_IMG = "img";
    private static final String UPLOADFILE_TYPE_EXCEL = "excel";
    private static final String UPLOADFILE_TYPE_WORD = "word";
    private static final String UPLOADFILE_TYPE_PDF = "pdf";
    private static final String UPLOADFILE_TYPE_PPT = "ppt";
    private static final String UPLOADFILE_TYPE_ZIP = "zip";
    private static final String UPLOADFILE_TYPE_VIDEO = "video";
    private static final String UPLOADFILE_TYPE_AUDIO = "audio";

    @Value("${project.uploadPath}")
    private static String uploadPath;

    /**
     * 图片后缀
     */
    private static final String IMAGE_SUFFIX = "jpg|jpge|png|gif";

    /**
     * excel后缀
     */
    private static final String EXCEL_SUFFIX = "xls|xlsx|xlsm";

    /**
     * word后缀
     */
    private static final String WORD_SUFFIX = "doc|docx";

    /**
     * ppt后缀
     */
    private static final String PPT_SUFFIX = "ppt|pptx";

    /**
     * pdf后缀
     */
    private static final String PDF_SUFFIX = "pdf";

    /**
     * 压缩后缀
     */
    private static final String ZIP_SUFFIX = "rar|zip";
    /**
     * 视频后缀
     */
    private static final String VIDEO_SUFFIX = "mp4|avi|rm|rmvb|wmv|3gp|mov|flv";
    /**
     * 音频后缀
     */
    private static final String AUDIO_SUFFIX = "mp3|wav|ogg|m4a";

    /**
     * 创建文件目录
     *
     * @param realPath
     *            项目绝对路径
     * @param catalogName
     *            目录名
     * @return
     */
    public static String createFileCatalog(String realPath, String catalogName) {
        String filePath = "/" + catalogName +  DateUtil.format(DateTime.now(), "/yyyyMMdd/");
        cn.hutool.core.io.FileUtil.mkdir(new File(realPath + filePath));
        return filePath;
    }

    /**
     * 文件类型后缀匹配
     *
     * @param fileType
     * @return
     */
    public static Boolean checkFileSuffix(String fileType, String suffix) {
        if (StringUtils.isNotEmpty(fileType)){
            String[] types = fileType.split("#");
            for (String type : types) {
                if (UPLOADFILE_TYPE_IMG.equals(type) && IMAGE_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                } else if (UPLOADFILE_TYPE_EXCEL.equals(type) && EXCEL_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                } else if (UPLOADFILE_TYPE_WORD.equals(type) && WORD_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                } else if (UPLOADFILE_TYPE_PDF.equals(type) && PDF_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                } else if (UPLOADFILE_TYPE_PPT.equals(type) && PPT_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                } else if (UPLOADFILE_TYPE_ZIP.equals(type) && ZIP_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                }else if (UPLOADFILE_TYPE_VIDEO.equals(type) && VIDEO_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                }else if (UPLOADFILE_TYPE_AUDIO.equals(type) && AUDIO_SUFFIX.indexOf(suffix.toLowerCase()) != -1) {
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }

    }

}
