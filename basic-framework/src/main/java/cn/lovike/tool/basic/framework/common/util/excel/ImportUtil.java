package cn.lovike.tool.basic.framework.common.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/****************************************
 * @@CREATE : 2018-08-23 上午11:02
 * @@AUTH : NOT A CAT【NOTACAT@CAT.ORZ】
 * @@DESCRIPTION : Excel、CSV 导出
 * @@VERSION :
 *
 *****************************************/
public class ImportUtil {
    private ImportUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ImportUtil.class);

    public static final String EXCEL_SUFFIX_XLS  = "xls";
    public static final String EXCEL_SUFFIX_XLSX = "xlsx";

    public static <T> Result<List<T>> importExcel(String suffix, ImportExcelCall<T> importExcelCall,
                                                  InputStream inputStream) throws Exception {

        if (null == inputStream) {
            throw new IOException("INPUT_NOT_EXIST");
        }
        if (!EXCEL_SUFFIX_XLS.equals(suffix) && !EXCEL_SUFFIX_XLSX.equals(suffix)) {
            throw new IllegalArgumentException("XLS_OR_XLSX");
        }

        Workbook wb = EXCEL_SUFFIX_XLS.equals(suffix) ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);

        try {
            Sheet         sheet = wb.getSheetAt(0);
            List<T>       list  = new ArrayList<>();
            StringBuilder error = new StringBuilder();

            for (Row row : sheet) {
                // 首行标题
                if (row.getRowNum() < 1) {
                    continue;
                }
                Result<T> rowResult = importExcelCall.call(row);
                if (rowResult.isSuccess()) {
                    list.add(rowResult.getData());
                } else {
                    error.append(rowResult.getMsg());
                }
            }
            if (error.length() > 0) {
                return Result.fail(error.toString());
            } else {
                return Result.success(list);
            }
        } catch (Exception e) {
            logger.error("导入excel异常", e);
            throw new RuntimeException("EXCEL_EXCEPTION", e);
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                logger.error("CLOSE_EXCEPTION", e);
            }
        }
    }

    @FunctionalInterface
    public interface ImportExcelCall<T> {
        Result<T> call(Row row);
    }


    public static class Result<T> {

        private boolean success;

        private T data;

        private String msg;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public static <T> Result<T> success(T data) {
            Result<T> result = new Result<T>();
            result.success = true;
            result.data = data;
            return result;
        }

        public static <T> Result<T> fail(String msg) {
            Result<T> result = new Result<T>();
            result.success = false;
            result.msg = msg;
            return result;
        }
    }

    // public static String getStringCellVal(Cell cell) {
    //     if (cell == null) {
    //         return null;
    //     }
    //
    //     switch (cell.getCellType()) {
    //         case Cell.CELL_TYPE_NUMERIC:
    //             if (DateUtil.isCellDateFormatted(cell)) {
    //                 Date date = cell.getDateCellValue();
    //                 if (date != null) {
    //                     return DateUtils.formatDate(date);
    //                 }
    //                 return null;
    //             }
    //             cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    //             return cell.getStringCellValue();
    //         case Cell.CELL_TYPE_STRING:
    //             return cell.getStringCellValue();
    //         case Cell.CELL_TYPE_FORMULA:
    //             return cell.getCellFormula();
    //         case Cell.CELL_TYPE_BLANK:
    //             return null;
    //         case Cell.CELL_TYPE_BOOLEAN:
    //             return String.valueOf(cell.getBooleanCellValue());
    //         case Cell.CELL_TYPE_ERROR:
    //             return String.valueOf(cell.getErrorCellValue());
    //         default:
    //             return null;
    //     }
    // }
}
