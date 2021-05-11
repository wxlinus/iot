package io.iot.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * TODO 物资管理工具类
 */
@SuppressWarnings("all")
public class WareHouseUtils {
    //仓库单据编号
    public static final String CKBILLCARD = "CK-";
    //供应商单据编号
    public static final String GYBILLCARD = "GY-";
    //供应物资类型编号
    public static final String WZLXBILLCARD = "WZLX-";
    //供应物资单位编号
    public static final String WZDWBILLCARD = "WZDW-";
    //物资出入库类型编号
    public static final String WZCRKBILLCARD = "WZCRLX-";
    //物资入库编号
    public static final String WZRKBILLCARD = "WZRK-";
    //物资出库编号
    public static final String WZCKBILLCARD = "WZCK-";
    //物资库存编号
    public static final String WZKCBILLCARD = "KC-";
    //物资编号
    public static final String WZBILLCARD = "WZ-";
    //物资订单编号
    public static final String DDBILLCARD = "DD-";
    //物资盘点编号
    public static final String PDBILLCARD = "PD-";
    //物资盘点图片编号
    public static final String PDIMGBILLCARD = "PDTP-";
    // 巡检设备类别编号
    public static final String XJSBLBBILLCARD = "SBLB-";
    // 巡检设备编号
    public static final String XJSBBILLCARD = "SB-";
    // 巡检设备计划编号
    public static final String XJSBJHBILLCARD = "SBXJJH-";
    /****************************************************************路径常量START******************************************************************************/
    // 库存物资导出路径 add by libaogang 2020/3/13
    public static final String stockExportExcelPath = "C:" + File.separator + "exportTemplete" + File.separator + "库存物资导出表模板.xlsx";

    //出入库登记表格保存路径
    public static final String exportExcelPath = "C:" + File.separator + "exportRegisiterTable" + File.separator;
    //盘点记录表保存路径
    public static final String exportInventoryExcelPath = "C:" + File.separator + "exportRegisiterTable" + File.separator;
    //出入库登记表格模板路径
    public static final String exportTempleExcelPath = "C:" + File.separator + "exportTemplete" + File.separator + "出入库登记表格模板.xlsx";
    //盘点记录表模板路径
    public static final String exportInventoryTempleExcelPath = "C:" + File.separator + "exportTemplete" + File.separator + "盘点记录表模板.xlsx";
    //盘点图片上传路径
    public static final String uploadInventoryImgPath = "C:" + File.separator + "uploadInventoryImgPath" + File.separator;
    // 巡检计划导出路径
    public static final String CLAIM_PLAN_EXPORT_PATH = "C:" + File.separator + "claimplanexportpath" + File.separator;
    // 巡检记录图片上传路径
//    public static final String PLAN_RECORDS_UPLOAD_PATH = "C:" + File.separator + "planrecordsuploadpath" + File.separator;
    /********************************************************************路径常量END*************************************************************************/

    /********************************************************************巡检计划定时提醒时间START*************************************************************************/
    public static final String FIRST_REMIND_DAY_AND_HOUR = "07" + " " + "00:00:00";
    public static final String SECOND_REMIND_DAY_AND_HOUR = "14" + " " + "00:00:00";
    // 今年所有月份
    public static final String JANUARY = "-" + "01" + "-" + "01" + " 00:00:00";
    public static final String FEBRUARY = "-" + "02" + "-" + "01" + " 00:00:00";
    public static final String MARCH = "-" + "03" + "-" + "01" + " 00:00:00";
    public static final String APRIL = "-" + "04" + "-" + "01" + " 00:00:00";
    public static final String MAY = "-" + "05" + "-" + "01" + " 00:00:00";
    public static final String JUNE = "-" + "06" + "-" + "01" + " 00:00:00";
    public static final String JULY = "-" + "07" + "-" + "01" + " 00:00:00";
    public static final String AUGUST = "-" + "08" + "-" + "01" + " 00:00:00";
    public static final String SEPTEMBER = "-" + "09" + "-" + "01" + " 00:00:00";
    public static final String OCTOBER = "-" + "10" + "-" + "01" + " 00:00:00";
    public static final String NOVEMBER = "-" + "11" + "-" + "01" + " 00:00:00";
    public static final String DECEMBER = "-" + "12" + "-" + "01" + " 00:00:00";
    // 今年所有季度
    public static final String FIRST_SEASON = "-" + "03" + "-";
    public static final String SECOND_SEASON = "-" + "06" + "-";
    public static final String THIRD_SEASON = "-" + "09" + "-";
    public static final String FOURTH_SEASON = "-" + "12" + "-";
    /********************************************************************巡检计划定时提醒时间END*************************************************************************/


    /***
     * TODO 返回需要的列表数据
     * @param list
     * @param firstPage
     * @param pageSize
     * @return
     */
    public static <T> List<T> returnNeedList(List<T> list, String firstPage, String pageSize) {
        List<T> queryList = new ArrayList<>();
        int pagesizelength = Integer.parseInt(pageSize);
        int pageYe = Integer.parseInt(firstPage);
        if (firstPage.equals("1")) {
            //若为第一页,那么就是从0,到10,并且考虑集合长度
            if (list.size() < pagesizelength * pageYe) {
                queryList = list.subList(0, list.size());
            } else {
                queryList = list.subList(0, pagesizelength);
            }
        } else {
            if (list.size() < pagesizelength * pageYe) {
                queryList = list.subList((pageYe - 1) * pagesizelength, list.size());
            } else {
                queryList = list.subList((pageYe - 1) * pagesizelength, pagesizelength * pageYe);
            }
        }
        return queryList;
    }

    /**
     * Date 转 localDate
     */
    public static LocalDate dateTOLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate localDate = zdt.toLocalDate();
        return localDate;
    }

    /**
     * 判断客户端浏览器类型
     *
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String UserAgent = request.getHeader("User-Agent").toLowerCase();
        if (UserAgent.indexOf("firefox") >= 0) {
            return "FF";
        } else if (UserAgent.indexOf("safari") >= 0) {
            return "Chrome";
        } else {
            return "IE";
        }
    }
}
