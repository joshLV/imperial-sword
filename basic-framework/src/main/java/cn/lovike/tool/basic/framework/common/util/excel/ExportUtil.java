package cn.lovike.tool.basic.framework.common.util.excel;

import com.opencsv.CSVWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/****************************************
 * @@CREATE : 2018-01-16 下午3:14
 * @@AUTH : NOT A CAT【NOTACAT@CAT.ORZ】
 * @@DESCRIPTION : Excel、CSV 导出
 * @@VERSION :
 *
 *****************************************/
public class ExportUtil {
    private ExportUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ExportUtil.class);

    public static final int EXCEL_ROW_ACCESS_WINDOW_SIZE = 1000;

    public static final String EXPORT_TYPE_EXCEL = "1";
    public static final String EXPORT_TYPE_CSV   = "2";

    public static final String EXCEL_SUFFIX_XLS    = "xls";
    public static final String EXCEL_SUFFIX_XLSX   = "xlsx";
    /**
     * xlsx 最大导出行数
     */
    public static final int    EXCEL_MAX_XLSX_ROWS = 1048576;
    /**
     * xls 最大导出行数
     */
    public static final int    EXCEL_MAX_XLS_ROWS  = 65536;
    /**
     * CSV 最大导出行数
     */
    public static final int    CSV_MAX_ROWS        = 1048576;

    /**
     * excel 导出功能
     *
     * @param titles          第一行标题
     * @param dataList        数据
     * @param exportExcelCall 回调
     * @param suffix          后缀
     * @param output          输出流
     * @param <T>
     */
    public static <T> void exportExcel(String[] titles, List<T> dataList,
                                       ExportExcelCall<T> exportExcelCall,
                                       String suffix, OutputStream output) {
        if (null == titles || 0 == titles.length) {
            throw new IllegalArgumentException("headers为空");
        }
        if (null == dataList) {
            throw new IllegalArgumentException("dataList为空");
        }
        if (null == exportExcelCall) {
            throw new IllegalArgumentException("exportExcelCall为空");
        }
        suffix = EXCEL_SUFFIX_XLS.equals(suffix) ? EXCEL_SUFFIX_XLS : EXCEL_SUFFIX_XLSX;
        // Excel 导出限制
        if ((EXCEL_SUFFIX_XLS.equals(suffix) && dataList.size() > EXCEL_MAX_XLS_ROWS - 1)
                || (EXCEL_SUFFIX_XLSX.equals(suffix) && dataList.size() > EXCEL_MAX_XLSX_ROWS - 1)) {
            throw new IllegalArgumentException("EXCEL导出" + suffix + "格式数据超出限制，禁止导出，数据量：" + dataList.size());
        }
        // 设置工作薄
        Workbook wb = EXCEL_SUFFIX_XLS.equals(suffix) ? new HSSFWorkbook() : new SXSSFWorkbook(EXCEL_ROW_ACCESS_WINDOW_SIZE);

        Sheet sheet = wb.createSheet();
        // 创建标题行及单元格并填充数据
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
        }

        // 创建数据行及单元格并填充数据
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Row                 bodyRow = sheet.createRow(i + 1);
                Map<String, String> map     = exportExcelCall.call(titles, dataList.get(i));
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = bodyRow.createCell(j);
                    cell.setCellValue(map.get(titles[j]));
                }
            }
        }

        try {
            wb.write(output);
            if (wb instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) wb).dispose();
            }
        } catch (IOException e) {
            logger.error("导出excel异常", e);
            throw new RuntimeException("导出excel异常", e);
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                logger.error("关闭excel异常", e);
            }
        }
    }

    /**
     * excel 导出功能
     *
     * @param titles          第一行标题
     * @param dataList        数据
     * @param exportExcelCall 回调
     * @param suffix          后缀
     * @param output          输出流
     * @param <T>
     */
    public static <T> void exportExcelVsStyle(String[] titles, List<T> dataList,
                                              ExportExcelCall<T> exportExcelCall,
                                              String suffix, OutputStream output) {
        if (null == titles || 0 == titles.length) {
            throw new IllegalArgumentException("headers为空");
        }
        if (null == dataList) {
            throw new IllegalArgumentException("dataList为空");
        }
        if (null == exportExcelCall) {
            throw new IllegalArgumentException("exportExcelCall为空");
        }
        suffix = EXCEL_SUFFIX_XLS.equals(suffix) ? EXCEL_SUFFIX_XLS : EXCEL_SUFFIX_XLSX;
        // Excel 导出限制
        if ((EXCEL_SUFFIX_XLS.equals(suffix) && dataList.size() > EXCEL_MAX_XLS_ROWS - 1)
                || (EXCEL_SUFFIX_XLSX.equals(suffix) && dataList.size() > EXCEL_MAX_XLSX_ROWS - 1)) {
            throw new IllegalArgumentException("EXCEL导出" + suffix + "格式数据太大，禁止导出，数据量" + dataList.size());
        }
        Workbook wb = EXCEL_SUFFIX_XLS.equals(suffix) ? new HSSFWorkbook() : new SXSSFWorkbook(EXCEL_ROW_ACCESS_WINDOW_SIZE);

        Sheet      sheet     = wb.createSheet();
        CellStyle  textStyle = wb.createCellStyle();
        DataFormat format    = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        // 创建标题行及单元格并填充数据
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(titles[i]);
            sheet.setDefaultColumnStyle(i, textStyle);
        }

        // 创建数据行及单元格并填充数据
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Row                 bodyRow = sheet.createRow(i + 1);
                Map<String, String> map     = exportExcelCall.call(titles, dataList.get(i));
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = bodyRow.createCell(j);
                    cell.setCellValue(map.get(titles[j]));
                    cell.setCellStyle(textStyle);
                }
            }
        }
        try {
            wb.write(output);
            if (wb instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) wb).dispose();
            }
        } catch (IOException e) {
            logger.error("导出excel异常", e);
            throw new RuntimeException("导出excel异常", e);
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                logger.error("关闭excel异常", e);
            }
        }
    }

    /**
     * csv 导出功能
     *
     * @param titles        第一行标题
     * @param dataList      数据
     * @param exportCSVCall 回调
     * @param output        输出流
     * @param <T>
     */
    public static <T> void exportCSV(String[] titles, List<T> dataList, ExportCSVCall<T> exportCSVCall, OutputStream output) {
        if (null == titles || 0 == titles.length) {
            throw new IllegalArgumentException("titles 为空");
        }
        if (null == dataList) {
            throw new IllegalArgumentException("dataList 为空");
        }
        if (null == exportCSVCall) {
            throw new IllegalArgumentException("exportCSVCall");
        }
        if (dataList.size() > CSV_MAX_ROWS - 1) {
            throw new IllegalArgumentException("CSV 导出数据太大，禁止导出，数据量" + dataList.size());
        }
        OutputStreamWriter osw = null;
        BufferedWriter     bfw = null;
        CSVWriter          csw = null;

        try {
            // 创建标题行及单元格并填充数据
            osw = new OutputStreamWriter(output, "GB18030");
            bfw = new BufferedWriter(osw);
            csw = new CSVWriter(bfw);
            csw.writeNext(titles);
            // 创建数据行及单元格并填充数据
            for (T t : dataList) {
                String[] dataStr = exportCSVCall.call(t);
                csw.writeNext(dataStr);
            }
        } catch (Exception e) {
            logger.error("导出CSV异常", e);
            throw new RuntimeException("导出CSV异常", e);
        } finally {
            try {
                if (null != csw) {
                    csw.close();
                }
            } catch (IOException e) {
                logger.error("关闭csv异常", e);
            }
        }
    }


    /**
     * excel 导出功能
     *
     * @param titles          第一行标题
     * @param mergeCnt        第一行标题合并单元格数
     * @param secondHeaders   第二行标题
     * @param dataList        数据
     * @param exportExcelCall 回调
     * @param suffix          后缀
     * @param output          输出流
     * @param <T>
     */
    public static <T> void exportExcelHeaderStyle(String[] titles, int mergeCnt,
                                                  String[] secondHeaders,
                                                  List<T> dataList, ExportExcelCallList<T> exportExcelCall,
                                                  String suffix, OutputStream output) {
        if (null == titles || 0 == titles.length) {
            throw new IllegalArgumentException("titles 为空");
        }
        if (null == secondHeaders || 0 == secondHeaders.length) {
            throw new IllegalArgumentException("secondHeaders 为空");
        }
        if (null == dataList) {
            throw new IllegalArgumentException("dataList 为空");
        }
        if (null == exportExcelCall) {
            throw new IllegalArgumentException("exportExcelCall 为空");
        }
        suffix = EXCEL_SUFFIX_XLS.equals(suffix) ? EXCEL_SUFFIX_XLS : EXCEL_SUFFIX_XLSX;
        // Excel 导出限制
        if ((EXCEL_SUFFIX_XLS.equals(suffix)
                && dataList.size() > EXCEL_MAX_XLS_ROWS - 1)
                || (EXCEL_SUFFIX_XLSX.equals(suffix)
                && dataList.size() > EXCEL_MAX_XLSX_ROWS - 1)) {
            throw new IllegalArgumentException("EXCEL导出" + suffix + "格式数据太大，禁止导出，数据量" + dataList.size());
        }
        Workbook wb = EXCEL_SUFFIX_XLS.equals(suffix) ? new HSSFWorkbook() : new SXSSFWorkbook(EXCEL_ROW_ACCESS_WINDOW_SIZE);

        Sheet sheet = wb.createSheet();

        // 一般双表头第一格为空，合并单元格
        for (int i = 0; i < titles.length; i++) {
            CellRangeAddress region = new CellRangeAddress(0, 0, mergeCnt * i + 1, mergeCnt * (i + 1));
            sheet.addMergedRegion(region);
        }

        // 创建标题行及单元格并填充数据
        Row headRow       = sheet.createRow(0);
        Row secondHeadRow = sheet.createRow(1);

        // 设置第一行标题
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headRow.createCell(i * mergeCnt + 1);
            cell.setCellValue(titles[i]);
        }

        // 设置第二行标题
        for (int i = 0; i < secondHeaders.length; i++) {
            Cell cell = secondHeadRow.createCell(i);
            cell.setCellValue(secondHeaders[i]);
        }

        // 创建数据行及单元格并填充数据
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Row          bodyRow = sheet.createRow(i + 2);
                List<String> list    = exportExcelCall.call(secondHeaders, dataList.get(i));
                for (int j = 0; j < secondHeaders.length; j++) {
                    Cell cell = bodyRow.createCell(j);
                    cell.setCellValue(list.get(j));
                }
            }
        }
        try {
            wb.write(output);
            if (wb instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) wb).dispose();
            }
        } catch (IOException e) {
            logger.error("导出excel异常", e);
            throw new RuntimeException("导出excel异常", e);
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                logger.error("关闭excel异常", e);
            }
        }
    }

    /**
     * Excel 导出回调接口
     *
     * @param <V>
     */
    @FunctionalInterface
    public interface ExportExcelCall<V> {
        /**
         * @param headers 第一行标题
         * @param v       JavaBean
         * @return
         */
        Map<String, String> call(String[] headers, V v);
    }

    /**
     * Excel 导出回调接口
     *
     * @param <V>
     */
    @FunctionalInterface
    public interface ExportExcelCallList<V> {
        /**
         * @param headers 第一行标题
         * @param v       JavaBean
         * @return
         */
        List<String> call(String[] headers, V v);
    }

    /**
     * CSV 导出回调接口
     *
     * @param <V>
     */
    @FunctionalInterface
    public interface ExportCSVCall<V> {
        /**
         * @param v JavaBean
         * @return
         */
        String[] call(V v);
    }
}
