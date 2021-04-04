package cn.lovike.tool.basic.framework.common.util.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author jinsong.zhan
 */
public class ReadExcelManager {
    private ReadExcelManager() {
    }

    public static final String BATCH_IMPORT_DT_FORMAT = "yyyy/MM/dd";
    public static final String BATCH_IMPORT_MERGED_CELL_VALUE = "merged";

    /**
     * 对外提供读取excel 的方法
     */
    public static List<SheetObj<?>> readExcel(String filePath) throws IOException, FileTypeErrorException {
        try {
            return read2007Excel(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return read2003Excel(filePath);
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new FileTypeErrorException("NOT_FILE_TYPE");
            }
        }
    }

    /**
     * 读取Office 2007 excel
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static List<SheetObj<?>> read2007Excel(String filePath) throws Exception {
        List<SheetObj<?>> listsheet = new LinkedList<SheetObj<?>>();
        FileInputStream fileInputStream = null;
        try {
            File file = new File(filePath);
            fileInputStream = new FileInputStream(file);
            XSSFWorkbook xwb = new XSSFWorkbook(fileInputStream);
            // 读取每个sheet
            for (int i = 0; i < xwb.getNumberOfSheets(); i++) {
                SheetObj<Object> sheetObj = getAllSheetObj(xwb.getSheetAt(i));
                if (sheetObj != null && sheetObj.getObjInfos() != null
                        && sheetObj.getData() != null) {
                    listsheet.add(sheetObj);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != fileInputStream) {
                fileInputStream.close();
            }

        }

        return listsheet;
    }

    /**
     * 读取excel中的全部sheet 2007
     *
     * @param xwb
     */
    private static SheetObj<Object> getAllSheetObj(XSSFSheet sheet) {
        SheetObj<Object> sheetObj = new SheetObj<Object>();
        sheetObj.setSheetName(sheet.getSheetName());
        sheetObj.setTitleName("");
        XSSFRow row = null;
        XSSFCell cell = null;
        /**
         * 加载表头
         */
        int titleNameNum = 0;// 起始表头所在的行号

        row = sheet.getRow(titleNameNum);
        if (row != null) {
            List<ExProtObjInfo> objInfos = new ArrayList<ExProtObjInfo>();
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {// 遍历该行的列
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }

                // 字段名为空也不保存
                if ("".equals(cell.toString().trim())) {
                    continue;
                }

                ExProtObjInfo objInfo = new ExProtObjInfo();
                objInfo.setEpoiCname(getCell(cell));
                objInfo.setEpoiEname(objInfo.getEpoiCname());
                objInfos.add(objInfo);
            }
            sheetObj.setObjInfos(objInfos);
        }

        /**
         * 加载数据
         */
        List<Object> data = new ArrayList<Object>();
        List<ExProtObjInfo> objInfos = sheetObj.getObjInfos();
        //雅昌的数据第一行表头中文，从第二行开始读取数据
        for (int i = titleNameNum + 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }
            if (row == null) {
                continue;
            }
            Map<String, String> map = new HashMap<String, String>();
            int counts = 0;  //保存成功的数量
            for (int j = 0; j < row.getLastCellNum(); j++) {
                if (isMergedRegion(sheet, i, j)) {        //如果是合并单元格中的cell，将值设置为BATCH_IMPORT_MERGED_CELL_VALUE
                    map.put(objInfos.get(counts).getEpoiEname(), BATCH_IMPORT_MERGED_CELL_VALUE);
                    counts++;
                    continue;
                }
                XSSFCell xssfCell = row.getCell(j);
                String value = xssfCell != null ? getCell(xssfCell) : "";
                if (sheet.getRow(titleNameNum).getCell(j) == null
                        || "".equals(sheet.getRow(titleNameNum).getCell(j).toString()
                        .trim())) {  //检查如果表头为空，不能保存该cell
                    continue;
                }
                map.put(objInfos.get(counts).getEpoiEname(), value);
                counts++;
            }
            data.add(map);
        }
        sheetObj.setData(data);
        return sheetObj;

    }

    /**
     * 根据excel导入单元格的格式。判断类型。并返回字符串 例如： String类型 return string; number类型 return
     * number+“”; Date 类型 return yyyy-MM-dd HH:mm:ss; 版本2007
     *
     * @param xCell
     * @return String
     */
    private static String getCell(XSSFCell xCell) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#");
        String cellvalue = "";
        if (xCell == null) {
            return "";
        }
        // 判断当前Cell的Type
        switch (xCell.getCellType()) {// 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_STRING: {
                cellvalue = xCell.getStringCellValue();
                break;
            }
            case HSSFCell.CELL_TYPE_NUMERIC: {
                // 处理日期数据 (如:2011-02-03)
                if (HSSFDateUtil.isCellDateFormatted(xCell)) {
                    double d = xCell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(d);
                    SimpleDateFormat dformat = new SimpleDateFormat(
                            BATCH_IMPORT_DT_FORMAT);
                    cellvalue = dformat.format(date);
                } else {
                    System.out.println(df.format(xCell.getNumericCellValue()));
                    String num = String.valueOf(xCell.getNumericCellValue());
                    String[] nums = StringUtils.split(num, ".");
                    if ("0".equals(nums[1])) { //如果小数点后面全是0
                        cellvalue = nums[0];
                    }
                    cellvalue = "0".equals(nums[1]) ? nums[0] : num;
                }
                break;
            }
            case HSSFCell.CELL_TYPE_FORMULA: {
                if (HSSFDateUtil.isCellDateFormatted(xCell)) {// 如果是Date类型则，取得该Cell的Date值
                    cellvalue = sdf.format(xCell.getDateCellValue().getTime());
                } else {// 如果是纯数字 取得当前Cell的数值
                    double num = xCell.getNumericCellValue();
                    cellvalue = String.valueOf(num);
                }
                break;
            }
            case HSSFCell.CELL_TYPE_BOOLEAN: {
                cellvalue = xCell.getBooleanCellValue() ? "true" : "false";
                break;
            }
            default:
                cellvalue = "";
        }

        // 将科学计数法转普通字段
//		if (null != cellvalue && cellvalue.indexOf(".") != -1
//				&& cellvalue.indexOf("E") != -1) {
//			DecimalFormat df = new DecimalFormat();
//			try {
//				cellvalue = df.parse(cellvalue).toString();
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
        return cellvalue.trim();
    }

    /**
     * 读取 office 2003 excel
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static List<SheetObj<?>> read2003Excel(String filePath) throws Exception {
        List<SheetObj<?>> listsheet = new LinkedList<SheetObj<?>>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(filePath));
            POIFSFileSystem poiFileSystem = new POIFSFileSystem(fileInputStream);
            HSSFWorkbook hwb = new HSSFWorkbook(poiFileSystem);

            for (int i = 0; i < hwb.getNumberOfSheets(); i++) {
                SheetObj<Object> sheetObj = getAllSheetObj(hwb.getSheetAt(i));
                if (sheetObj != null && sheetObj.getObjInfos() != null
                        && sheetObj.getData() != null) {
                    listsheet.add(sheetObj);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != fileInputStream) {
                fileInputStream.close();
            }
        }
        return listsheet;
    }

    /**
     * 读取sheet中的数据，并且将数据封装成SheetObj对象 2003
     *
     * @param sheet
     * @return
     */
    private static SheetObj<Object> getAllSheetObj(HSSFSheet sheet) {
        SheetObj<Object> sheetObj = new SheetObj<Object>();
        sheetObj.setSheetName(sheet.getSheetName());
        sheetObj.setTitleName("");
        HSSFRow row = null;
        HSSFCell cell = null;

        /**
         * 加载表头
         */
        int titleNameNum = 0;
        row = sheet.getRow(titleNameNum);
        if (row != null) {
            List<ExProtObjInfo> objInfos = new ArrayList<ExProtObjInfo>();
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }

                // 字段名为空也不保存
                if ("".equals(cell.toString().trim())) {
                    continue;
                }

                ExProtObjInfo objInfo = new ExProtObjInfo();
                objInfo.setEpoiCname(getCell(cell));
                objInfo.setEpoiEname(objInfo.getEpoiCname());
                objInfos.add(objInfo);

            }
            sheetObj.setObjInfos(objInfos);
        }

        /**
         * 加载数据
         */
        List<Object> data = new ArrayList<Object>();
        List<ExProtObjInfo> objInfos = sheetObj.getObjInfos();
        //模板第一行为表头，数据从第二行开始
        for (int i = titleNameNum + 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }
            if (row == null) {
                continue;
            }
            Map<String, String> map = new HashMap<String, String>();
            int counts = 0;  //保存成功的数量
            for (int j = 0; j < row.getLastCellNum(); j++) {
                if (isMergedRegion(sheet, i, j)) {        //如果是合并单元格中的cell，将值设置为BATCH_IMPORT_MERGED_CELL_VALUE
                    map.put(objInfos.get(counts).getEpoiEname(), BATCH_IMPORT_MERGED_CELL_VALUE);
                    counts++;
                    continue;
                }
                HSSFCell hssfCell = row.getCell(j);
                if (hssfCell != null) {
                    hssfCell.setCellType(1);
                }
                String value = hssfCell != null ? getCell(hssfCell) : "";
                if (sheet.getRow(titleNameNum).getCell(j) == null
                        || "".equals(sheet.getRow(titleNameNum).getCell(j).toString()
                        .trim())) {  //检查如果表头为空，不能保存该cell
                    continue;
                }
                map.put(objInfos.get(counts).getEpoiEname(), value);
                counts++;
            }
            data.add(map);
        }
        sheetObj.setData(data);
        return sheetObj;
    }

    /**
     * 根据excel导入单元格的格式。判断类型。并返回字符串 例如： String类型 return string; number类型 return
     * number+“”; Date 类型 return yyyy-MM-dd HH:mm:ss; 版本2003
     *
     * @param cell
     * @return String
     */
    private static String getCell(HSSFCell cell) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cellvalue = "";
        if (cell == null) {
            return "";
        }
        // 判断当前Cell的Type
        switch (cell.getCellType()) {// 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_STRING: {
                cellvalue = cell.getStringCellValue();
                break;
            }
            case HSSFCell.CELL_TYPE_NUMERIC: {
                String num = cell.getNumericCellValue() + "";
                String[] nums = StringUtils.split(num, ".");
                cellvalue = "0".equals(nums[1]) ? nums[0] : num;
                break;
            }
            case HSSFCell.CELL_TYPE_FORMULA: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 如果是Date类型则，取得该Cell的Date值
                    cellvalue = sdf.format(cell.getDateCellValue().getTime());
                } else {// 如果是纯数字 取得当前Cell的数值
                    double num = (double) cell.getNumericCellValue();
                    cellvalue = String.valueOf(num);
                }
                break;
            }

            case HSSFCell.CELL_TYPE_BOOLEAN: {
                cellvalue = cell.getBooleanCellValue() ? "true" : "false";
                break;
            }
            default:
                cellvalue = "";
        }

        // 将科学计数法转普通字段
//		if (null != cellvalue && cellvalue.indexOf(".") != -1
//				&& cellvalue.indexOf("E") != -1) {
//			DecimalFormat df = new DecimalFormat();
//			try {
//				cellvalue = df.parse(cellvalue).toString();
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}

        return cellvalue.trim();
    }

    /**
     * 返回当前sheet的表头数据的起始行，用于便利真正的表头 版本2003
     *
     * @param sheet
     * @return
     */
    private static int getTileNameNum(HSSFSheet sheet) {
        int tileNameNum = 0;
        HSSFRow row = null;
        row = sheet.getRow(0);
        if (row != null) {
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                if ("".equals(getCell(row.getCell(i)))) {
                    tileNameNum++;
                    break;
                }
            }
        }
        return tileNameNum;

    }

    /**
     * 导出的数据对象字段类型标示转换
     *
     * @param type
     * @return
     */
    public static Integer epoiTypeString2Int(String type) {
        int typeNum = 1;
        if (type != null && !"".equals(type)) {
            if ("INT".equalsIgnoreCase(type)
                    || "INTEGER".equalsIgnoreCase(type)) {
                typeNum = 0;
            } else if ("STRING".equalsIgnoreCase(type)) {
                typeNum = 1;
            } else if ("TIME".equalsIgnoreCase(type)) {
                typeNum = 2;
            } else if ("DATE".equalsIgnoreCase(type)) {
                typeNum = 3;
            } else if ("DATETIME".equalsIgnoreCase(type)) {
                typeNum = 4;
            } else if ("BOOLEAN".equalsIgnoreCase(type)) {
                typeNum = 5;
            } else if ("DOUBLE".equalsIgnoreCase(type)) {
                typeNum = 6;
            }
        } else {
            typeNum = 1;
        }

        return typeNum;

    }

    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row == firstRow && column == firstColumn) {        //如果是合并单元格的第一行和第一列，就不是被合并的单元格
                return false;
            }
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }
}
