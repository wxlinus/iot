package io.iot.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.io.FileUtil;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.write.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ExcelUtil {
	
	/**
	 * 创建excel表格
	 * @param url文件地址
	 * @param list表格属性名
	 * @throws WriteException
	 * @throws IOException
	 */
    public static String createExcel(String url, String sheetname, String tittle, List<String> name, List<Map> list) throws WriteException, IOException {
    	File file = FileUtil.file(url);
    	String path = file.getPath();
    	//判断文件是否存在，是否是文件夹
        if (!file.exists() || file.isDirectory()) {
        	file.getParentFile().mkdirs();
        }else {
        	file.delete();
        }
    	FileOutputStream fileOut = new FileOutputStream(path);
        //创建工作薄
        WritableWorkbook workbook = jxl.Workbook.createWorkbook(fileOut);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet(sheetname,0);
        
      //设置打印属性  
        sheet.getSettings().setOrientation(PageOrientation.LANDSCAPE) ;// 设置为横向打印  
        sheet.getSettings().setPaperSize(PaperSize.A4);//设置纸张A4  
        sheet.getSettings().setFitHeight(297);//打印区高度  
        sheet.getSettings().setFitWidth(210) ;//打印区宽度
        
     // 设置边距              
        sheet.getSettings().setTopMargin(0.5) ;  
        sheet.getSettings().setBottomMargin(0.3) ;  
        sheet.getSettings().setLeftMargin(0.1) ;  
        sheet.getSettings().setRightMargin(0.1) ;
        
     // 第三部,设置表格指定列的列宽
        sheet.setColumnView(0, 20);
        sheet.setColumnView(1, 40);
        sheet.setColumnView(2, 20);
        sheet.setColumnView(3, 20);
        sheet.setColumnView(4, 20);
        sheet.setColumnView(5, 20);
        sheet.setColumnView(6, 20);

        // 第四部,往工作簿中插入数据,设定字体:微软雅黑,24,加粗
        // 创建字体对象
        WritableFont titleFont = new WritableFont(WritableFont.createFont("微软雅黑"), 24, WritableFont.NO_BOLD);
        WritableFont contentFont = new WritableFont(WritableFont.createFont("楷体 _GB2312"), 12, WritableFont.NO_BOLD);
        WritableFont contentFont2 = new WritableFont(WritableFont.createFont("楷体 _GB2312"), 12, WritableFont.BOLD);
        WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
        titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);//设置边框样式  
        WritableCellFormat contentFormat = new WritableCellFormat(contentFont);
        WritableCellFormat contentFormat2 = new WritableCellFormat(contentFont2);
        contentFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);//设置边框样式  
        WritableCellFormat contentFormat3 = new WritableCellFormat(contentFont2);
        
        contentFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        // 设置格式居中对齐
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        contentFormat.setAlignment(jxl.format.Alignment.CENTRE);
        contentFormat2.setAlignment(jxl.format.Alignment.CENTRE);
        
        //设置表头
        sheet.mergeCells(0, 0, name.size()-1, 0); // 合并单元格
        sheet.addCell(new Label(0, 0, tittle, titleFormat));
        
        //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        for (int i = 0; i < name.size(); i++) {			
        	Label lablename = new Label(i,1,name.get(i),contentFormat2);
        	sheet.addCell(lablename);
		}
        //添加属性值
        for (int j = 0; j < list.size(); j++) {
        	Map map = list.get(j);
        	for (int i = 0; i < name.size(); i++) {			
            	Label lablename = new Label(i,j+2, String.valueOf(map.get(name.get(i))),contentFormat);
            	sheet.addCell(lablename);
    		}
        }

        sheet.addCell(new Label(3, list.size()+3, "签名:", contentFormat3));
        sheet.mergeCells(4, list.size()+3, 5, list.size()+3); // 合并单元格
        sheet.addCell(new Label(4, list.size()+3, "", contentFormat3));
        
        sheet.addCell(new Label(3, list.size()+4, "日期:", contentFormat3));
        sheet.mergeCells(4, list.size()+4, 5, list.size()+4); // 合并单元格
        sheet.addCell(new Label(4, list.size()+4, "", contentFormat3));
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        fileOut.close();
        return path;
    }
    
    /**
     * excel 导出
     *
     * @param list           数据
     * @param title          标题
     * @param sheetName      sheet名称
     * @param pojoClass      pojo类型
     * @param fileName       文件名称
     * @param isCreateHeader 是否创建表头
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list      数据
     * @param title     标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  文件名称
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }

    /**
     * excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param response
     * @param exportParams 导出参数
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param response
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param response
     * @param exportParams 导出参数
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param response
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 下载
     *
     * @param fileName 文件名称
     * @param response
     * @param workbook excel数据
     */
    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param filePath   excel文件路径
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedSave(true);
        params.setSaveUrl("/excel/");
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param file      excel文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws IOException {
        return importExcel(file, 1, 1, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       excel文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        return importExcel(file, titleRows, headerRows, false, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       上传的文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param needVerfiy 是否检验excel内容
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, boolean needVerfiy, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), titleRows, headerRows, needVerfiy, pojoClass);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param titleRows   标题行
     * @param headerRows  表头行
     * @param needVerfiy  是否检验excel内容
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows, boolean needVerfiy, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setSaveUrl("/excel/");
        params.setNeedSave(true);
        params.setNeedVerify(needVerfiy);
        try {
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("excel文件不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Excel 类型枚举
     */
    enum ExcelTypeEnum {
        XLS("xls"), XLSX("xlsx");
        private String value;

        ExcelTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
