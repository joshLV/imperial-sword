package cn.lovike.tool.basic.framework.common.util.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XlsFileUtil {
    private XlsFileUtil() {
    }

    private static String[] cellPicture = new String[] { "n|1|整数", "n2|2|两位小数", "n4|180|四位小数", "n6|181|六位小数",
            "s|49|文本", "m|4|货币", "d|14|日期" };

    /**
     * 设置单元格的边框
     *
     * @param style 单元格格式对象
     * @param isTitle 是否是标题栏（标题栏设置背景色）
     */
    private static void setBorder(HSSFCellStyle style, boolean isTitle) {
        style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 上边框
        style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);// 下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边框
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边框

        if (isTitle) {
            // 设置背景色
            style.setFillForegroundColor(HSSFColor.YELLOW.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }
    }

    /**
     * 设置单元格的值
     *
     * @param cell 单元格
     * @param cellType 单元格的预设格式
     * @param value 要填充的值
     */
    private static void setCellRealValue(HSSFCell cell, String cellType, String value) {
        if (StringUtils.isBlank(value)) {
            cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
            return;
        }
        if ("n".equals(cellType) || "n2".equals(cellType) || "n4".equals(cellType) || "n6".equals(cellType)
            || "m".equals(cellType)) {
            // 数字型
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(Double.parseDouble(value));
        } else if ("s".equals(cellType)) {
            // 字符型
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(value));
        } else if ("d".equals(cellType)) {
            // 日期型
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Date date = sdf.parse(value);
                cell.setCellValue(date);
            } catch (ParseException e) {
                cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * @param format
     * @param cellType
     */
    private static short getCellPicture(HSSFDataFormat format, String cellType) {
        short fmt = 49;// 默认文本格式
        if ("n".equals(cellType)) {
            // 整数型，如果为数字则去掉小数点后的部分
            fmt = format.getFormat("0");
        } else if ("n2".equals(cellType)) {
            // 两位小数
            fmt = format.getFormat("0.00");
        } else if ("n4".equals(cellType)) {
            // 四位小数
            fmt = format.getFormat("0.0000");
        } else if ("n6".equals(cellType)) {
            // 六位小数
            fmt = format.getFormat("0.000000");
        } else if ("s".equals(cellType)) {
            // 文本型
            fmt = 49;
        } else if ("m".equals(cellType)) {
            // 货币型（在EXCEL文件中，货币型的数据属于数值）
            fmt = 4;
        } else if ("d".equals(cellType)) {
            // 日期型
            fmt = 14;
        }
        return fmt;
    }

    /**
     * 检查EXCEL文件中表格里的数据是否符合格式
     *
     * @param fileType 预设的EXCLE列格式，目前仅支持"n|n2|n4|n6|s|m|d|"七种格式 n为整数（例：0） n2位两位小数（例：0.00） n4为4位小数（例：0.0000）
     * n6为6位小数（例：0.000000） s为文本（例：abc123） m为货币（例：-9,999.99） d为日期（例：2013-1-2）
     * @param firstRow 起始行数 从EXCEL文件中的第几行开始数据，用于区分有标题和无标题的情况，常用行数1或2
     * @return 如果文件格式正确，返回存储文件数据的链式列表；如果文件格式有误，提示错误并返回null（每行数据用数组存储）
     * @throws IOException
     */
    public static LinkedList<String[]> checkAndGetXlsFileData(String absFileName, String fileType, int firstRow)
                                                                                                                throws IOException {
        // 检查参数的有效性
        if (StringUtils.isBlank(fileType)) {
            return null;
        } else {
            if (0 > fileType.indexOf('|')) {
                return null;
            }
        }

        /*
         * 1.定义变量
         */
        FileInputStream stream = null;
        LinkedList<String[]> list = new LinkedList<String[]>(); // 储存文件数据的链式列表
        try {
            /*
             * 2.文件流，读入EXCEL文件
             */
            stream = new FileInputStream(absFileName);

            /*
             * 3.使用POI的包，按照EXCEL文件的组织形式得到文件的内容
             */
            HSSFWorkbook workBook = new HSSFWorkbook(stream);// 活动页
            HSSFSheet sheet = workBook.getSheetAt(0);// 默认只取第一页

            /*
             * 4.解析定义的文件格式
             */
            String[] fileTypeStrs = fileType.split("\\|");

            /*
             * 5.判断表格的列数与默认的文件格式是否相符
             */
            HSSFRow nRow = sheet.getRow(firstRow - 1);// 默认获得第一行
            // 判断列数是否与文件格式的个数相同
            int columnNum = nRow.getPhysicalNumberOfCells();// 通过行，获得Excel文件的列数
            if (columnNum != fileTypeStrs.length) {
                return null;
            }

            /*
             * 6.如果没有日期列，需要手工去除空数据（不然模板文件就会返回100行）
             */
            String[] tmpStrs = new String[fileTypeStrs.length];
            if (!fileType.contains("d")) {
                for (int i = 0; i < fileTypeStrs.length; i++) {
                    if ("n".equals(fileTypeStrs[i])) {
                        tmpStrs[i] = "0";
                    }
                    if ("n2".equals(fileTypeStrs[i]) || "m".equals(fileTypeStrs[i])) {
                        tmpStrs[i] = "0.00";
                    }
                    if ("n4".equals(fileTypeStrs[i])) {
                        tmpStrs[i] = "0.0000";
                    }
                    if ("n6".equals(fileTypeStrs[i])) {
                        tmpStrs[i] = "0.000000";
                    }
                    if ("s".equals(fileTypeStrs[i])) {
                        tmpStrs[i] = "";
                    }
                }
            }
            String nullData = stringArrayToCsv(tmpStrs);// 拼空数据的样子

            /*
             * 7.遍历文件，按行遍历，在得到文件的行后，通过遍历列的方式，得到文件中的一个单元格，检查单元格的格式并校验数据是否正确
             */
            for (int rowIndex = firstRow - 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                HSSFRow row = sheet.getRow(rowIndex);// 获得行

                // 定义一个跟列数相等的数组，用于存放行数据
                String[] rowData = new String[fileTypeStrs.length];

                // 遍历列
                for (int i = 0; i < fileTypeStrs.length; i++) {
                    HSSFCell cell = row.getCell(i);// 获得单元格

                    // 检查单元格格式、校验数据并返回正确的单元格的值
                    rowData[i] = checkAndGetCellValue(cell, fileTypeStrs[i]);
                    // 如果单元格数据为null，直接返回null
                    if (null == rowData[i]) {
                        return null;
                    }
                }

                if (nullData.equals(stringArrayToCsv(rowData))) {
                    // 去掉空数据
                    continue;
                }
                list.add(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 将数组转换成以，分割的字符串
     *
     * @param array
     * @return
     */
    private static String stringArrayToCsv(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]).append(",");
        }
        sb.append(array[array.length - 1]);
        return sb.toString();
    }

    /**
     * <b>将XLS文件转化成TXT文件</b>
     *
     * @param absUploadPath XLS文件在ABS上的路径
     * @param fileType XLS文件的数据格式
     * @param firstRow XLS文件正文开始的位置（从第几行开始，常用1或2）
     * @return true:xls转换txt成功 false:转换失败
     * @throws Exception
     */
    public static boolean tranXlsToTxt(String absUploadPath, String fileType, int firstRow) throws Exception {
        LinkedList<String[]> fileContent = XlsFileUtil.checkAndGetXlsFileData(absUploadPath, fileType, firstRow);

        // 新的文件路径
        String newFilePath = absUploadPath.replace(".xls", ".txt");

        // 字符串缓冲区
        StringBuilder bf = new StringBuilder();
        // 将.xls文件的内容写入.txt文件
        BufferedWriter writer = null;

        try {
            File file = new File(newFilePath);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
            // 将文件内容转换成一个数组，用于写入新的txt文件
            for (int i = 0; i < fileContent.size(); i++) {
                String[] data = fileContent.get(i);
                for (int j = 0; j < data.length; j++) {
                    bf.append(data[j]).append("|");
                }

                writer.write(bf.toString() + "\r\n");
                writer.flush();
                bf = new StringBuilder();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 删除旧文件
        File oldFile = new File(absUploadPath);
        if (oldFile.exists()) {
            oldFile.delete();
        }

        return true;
    }

    /**
     * 检查Excel文件单元格的格式，并返回符合格式的单元格的值
     *
     * @param cell 单元格
     * @param cellType 定义的单元格格式
     * @return 如果单元格格式正确，返回符合格式的单元格数据；否则，返回null
     * @throws IOException
     */
    private static String checkAndGetCellValue(HSSFCell cell, String cellType) throws IOException {
        // 定义最后要返回的单元格的值
        String cellValue = "";

        // 获得单元格的CellStyle
        HSSFCellStyle cellStyle = cell.getCellStyle();

        // 获得单元格数据格式的short格式代码
        String index = cellStyle.getDataFormat() + "";

        // 遍历工具类定义的静态的代表格式的数组
        for (int i = 0; i < cellPicture.length; i++) {
            // 检查该单元格的格式代码是否等于EXCEL文档该种预定义格式对饮的short代码
            String[] temp = cellPicture[i].split("\\|");
            if (cellType.equals(temp[0])) {
                if (!temp[1].equals(index)) {
                    // 不相符，判断表格是否是数字格式的
                    String regExs = ""; // 字符
                    Pattern p = null;
                    Matcher m = null;
                    if ("n".equals(cellType) || "n2".equals(cellType) || "n4".equals(cellType) || "n6".equals(cellType)) {
                        regExs = "[-.0-9]";
                        p = Pattern.compile(regExs);
                        m = p.matcher(cell.getNumericCellValue() + "");

                        if (!m.find()) {
                            return null;
                        }
                    } else if ("d".equals(cellType)) {
                        if (!"178".equals(index)) {// 模板文件手动输入日期的话，index是178
                            return null;
                        }
                    } else {
                        return null;
                    }
                }

                // 相符，按照相应格式的读取方式得到单元格的值；如果是文本格式，则需要检查内容里是否含有非法字符
                cellValue = getCellValue(cell, cellType);

                if ("s".equals(cellType)) {
                    String regExs = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; // 字符
                    Pattern p = null;
                    Matcher m = null;
                    p = Pattern.compile(regExs);
                    m = p.matcher(cellValue);
                    if (m.find()) {
                        return null;
                    }
                }
                break;
            }
        }
        return cellValue;
    }

    /**
     * 获得单元格的值
     *
     * @param cell 单元格
     * @param cellType 格式
     * @return 单元格的值
     * @throws IOException
     */
    private static String getCellValue(HSSFCell cell, String cellType) throws IOException {
        String str = "";
        if ("n".equals(cellType)) {
            DecimalFormat df = new DecimalFormat("#");
            // 整数型，如果为数字则去掉小数点后的部分
            str = df.format(cell.getNumericCellValue());
        } else if ("n2".equals(cellType)) {
            // 两位小数
            DecimalFormat df = new DecimalFormat("#.00");
            str = df.format(cell.getNumericCellValue());
        } else if ("n4".equals(cellType)) {
            DecimalFormat df = new DecimalFormat("#.0000");
            // 四位小数
            str = df.format(cell.getNumericCellValue());
        } else if ("n6".equals(cellType)) {
            DecimalFormat df = new DecimalFormat("#.000000");
            // 六位小数
            str = df.format(cell.getNumericCellValue());
        } else if ("s".equals(cellType)) {
            // 文本型
            str = cell.getRichStringCellValue().getString();
        } else if ("m".equals(cellType)) {
            // 货币型（在EXCEL文件中，货币型的数据属于数值）
            DecimalFormat df = new DecimalFormat("#.00");
            str = df.format(cell.getNumericCellValue());
        } else if ("d".equals(cellType)) {
            // 日期型
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
            str = s.format(cell.getDateCellValue());
        }
        return str;
    }

    /**
     * 设置导出EXCEL的文章正标题
     *
     * @param workbook
     * @param sheet
     * @param articleTitle 正标题名称
     * @param columnNum 文本内容的列数
     * @return
     */
    private static void createXlsHead(HSSFWorkbook workbook, HSSFSheet sheet, String articleTitle, int columnNum) {

        HSSFRow fileTitle = sheet.createRow(0);
        HSSFCell fileCell = fileTitle.createCell(0);
        setCellRealValue(fileCell, "s", articleTitle);
        CellRangeAddress address = new CellRangeAddress(0, 0, 0, columnNum - 1);
        sheet.addMergedRegion(address); // 合并单元格
        HSSFCellStyle fileTitleStyle = workbook.createCellStyle();
        fileTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        HSSFFont font = workbook.createFont(); // 设置字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 粗体
        font.setFontHeightInPoints((short) 19); // 字号
        fileCell.setCellStyle(fileTitleStyle);
        fileTitleStyle.setFont(font);

    }

    /**
     * 创建文件副标题或文件尾的内容 （支持多行）
     *
     * @param workbook
     * @param sheet
     * @param content 副标题或文件尾的内容 例如：content[0] = "流水号:XXXXX|批次号:XXXX|业务日期:XXXXX|"; content[1] =
     * "操作员:XXXXX|授权员:XXXX|打印日期:XXXXX|";
     * @param columnNum 文本内容的列数
     * @return
     */
    private static void createXlsPlusHeadOrTail(HSSFWorkbook workbook, HSSFSheet sheet, String[] content, int columnNum) {
        HSSFCellStyle msgStyle = workbook.createCellStyle();
        HSSFFont msgFont = workbook.createFont(); // 设置字体
        msgFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 粗体
        msgFont.setFontHeightInPoints((short) 10);

        // 创建标题的内容
        int currentRow = sheet.getLastRowNum() + 1;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < content.length; i++) {
            HSSFRow rowMsg = sheet.createRow(currentRow);
            String[] rowInfo = content[i].split("\\|");
            for (int j = 0; j < rowInfo.length; j++) {
                buffer.append(rowInfo[j]).append("     ");
            }
            HSSFCell cellMsg = rowMsg.createCell(0);
            setCellRealValue(cellMsg, "s", buffer.toString());

            // 设置副标题的格式
            CellRangeAddress address = new CellRangeAddress(currentRow, currentRow, 0, columnNum - 1);
            sheet.addMergedRegion(address); // 合并单元格

            // 设置样式和字体
            cellMsg.setCellStyle(msgStyle);
            msgStyle.setFont(msgFont);
            currentRow++;
            buffer.delete(0, buffer.length());
        }
    }

    /**
     * 创建符合预设格式的Excel文件
     *
     * @param destPath 指定路径
     * @param sheetMap 存储sheet页及数据的map容器
     * @return 文件是否创建成功
     * @throws IOException
     */
    public static boolean createXlsFileWithHeadAndTail(String destPath, Map<String, SheetData> sheetMap)
                                                                                                        throws Exception {
        // 创建文件
        HSSFWorkbook workbook = new HSSFWorkbook();// 创建工作簿

        for (Map.Entry<String, SheetData> entry : sheetMap.entrySet()) {
            final String key = entry.getKey();
            SheetData data = entry.getValue();
            /*
             * 生成符合预设格式的xls文件
             */
            HSSFSheet sheet = workbook.createSheet(key);// 创建工作表单

            String fileType = data.getFileType();
            String headContent = data.getHead();
            String[] plusHead = data.getPlusHead();
            String[] tailContent = data.getTail();
            String title = data.getTitle();
            String[] content = data.getContent();

            String[] format = fileType.split("\\|");// 文件预设格式数据
            /*
             * 1.输入条件判断：指定路径和预设格式不能为空
             */
            if (StringUtils.isBlank(destPath) || StringUtils.isBlank(fileType)) {
                return false;
            }

            /*
             * 2.根据title区分是否有表头：如果有，第一行设置为表头，数据从第二行开始填充；没有，数据则从第一行开始填充
             */
            int firstRow = 0;
            if (!StringUtils.isBlank(headContent)) {
                firstRow = 1;
            }
            if (plusHead != null && plusHead.length > 0) {
                firstRow = firstRow + plusHead.length;
            }
            String[] titleData = null;
            if (!StringUtils.isBlank(title)) {
                firstRow = firstRow + 1;
                // 检查标题栏与文件预设格式的列数是否一致
                titleData = title.split("\\|");// 标题栏数据

                if (titleData.length != format.length) {
                    return false;
                }
            }

            // 创建文件正标题
            if (!StringUtils.isBlank(headContent)) {
                createXlsHead(workbook, sheet, headContent, titleData.length);
            }
            // 创建文件的副标题
            if (plusHead != null && plusHead.length > 0) {
                createXlsPlusHeadOrTail(workbook, sheet, plusHead, titleData.length);
            }

            // 创建列标题
            if (firstRow != 0) {
                // 创建标题行，并设置行的数据格式为文本
                int currRowNum = sheet.getLastRowNum();

                HSSFRow row = null;
                if (StringUtils.isBlank(headContent) && plusHead == null) {
                    row = sheet.createRow(0);
                } else {
                    row = sheet.createRow(currRowNum + 1);
                }

                // 设置数据格式
                HSSFCellStyle style = workbook.createCellStyle();
                style.setDataFormat((short) 49);
                // 设置边框
                setBorder(style, true);
                // 设置居中
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                // 填充标题
                String[] temp = title.split("\\|");
                for (int i = 0; i < temp.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(new HSSFRichTextString(temp[i]));
                    cell.setCellStyle(style);
                }
            }

            // 填充文件内容
            int rowCount = firstRow;
            boolean contentIsNull = false;
            if (null == content || 0 == content.length) {
                // 如果文件内容为空，生成空的模板文件，默认行数设置为100行
                rowCount = 100;
                contentIsNull = true;
            } else {
                // 文件内容不为空，将文件内容填充到相应的单元格中并按照预设格式为单元格设置数据格式
                rowCount = content.length;
                contentIsNull = false;
            }

            List<HSSFCellStyle> listStyle = new ArrayList<HSSFCellStyle>();
            // 根据预设格式遍历列
            for (int j = 0; j < format.length; j++) {
                HSSFCellStyle style = workbook.createCellStyle();
                // 设置边框
                setBorder(style, false);

                // 对单元格的格式进行设置
                HSSFDataFormat f = workbook.createDataFormat();
                short fmt = getCellPicture(f, format[j]);
                style.setDataFormat(fmt);
                listStyle.add(style);
            }

            // 填充行，从firstRow开始，共rowCount行
            for (int i = firstRow; i < rowCount + firstRow; i++) {
                HSSFRow row = sheet.createRow(i);

                // 如果文件内容不为空，逐行读取
                String[] tmpRowData = null;
                if (!contentIsNull) {
                    tmpRowData = content[i - firstRow].split("\\|", -1);
                }

                // 根据预设格式遍历列
                for (int j = 0; j < format.length; j++) {
                    // 获得唯一单元格
                    HSSFCell cell = row.createCell(j);

                    // 对单元格设置值
                    setCellRealValue(cell, format[j], contentIsNull ? null : tmpRowData[j]);

                    cell.setCellStyle(listStyle.get(j));
                }
            }

            // 创建文件尾部内容
            if (tailContent != null && tailContent.length > 0) {
                createXlsPlusHeadOrTail(workbook, sheet, tailContent, titleData.length);
            }
        }

        // 输出文件
        FileOutputStream stream = null;
        File file = new File(destPath);
        if (file.exists()) {
            return true;
        }
        try {
            stream = new FileOutputStream(file);
            workbook.write(stream);
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (null != stream) {
                    stream.close();
                }
            } catch (IOException e) {
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SheetData d = new SheetData();
        d.setHead("风险预警统计表");
        // d.setPlusHead(new String[]{"时间：2015-02-28|统计：没有了"});
        d.setTitle("设备id|客户号|金额|风险类型|风险结果|其他|");
        d.setContent(new String[] { "erqweq|32423|1234|123|45|34|", "qwe|232|1232|123|454|tryetZ|" });
        // d.setTail(new String[] { "合计金额：2000.00|合计类型：没有" });
        d.setFileType("s|s|s|s|s|s|");

        Map<String, SheetData> m = new LinkedHashMap<String, SheetData>();
        m.put("按交易渠道统计", d);
        m.put("按交易类型统计", d);
        m.put("按交易时间统计", d);

        try {
            boolean flag = createXlsFileWithHeadAndTail("/Users/tongdun/Documents/Test1.xlsx", m);
            if (flag) {
                System.out.println("导出成功");
            } else {
                System.out.println("导出失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
