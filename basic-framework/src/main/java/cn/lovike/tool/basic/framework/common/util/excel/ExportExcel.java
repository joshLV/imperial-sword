package cn.lovike.tool.basic.framework.common.util.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * 利用开源组件 POI3.0.2 动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 *
 * @param <T> 应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 * @author leno
 * @version v1.0
 */
public class ExportExcel<T> {
    public void exportExcel(Collection<T> dataset, OutputStream out) {
        exportExcel("导出EXCEL文档", null, null, dataset, out, "yyyy-MM-dd");
    }

    public void exportExcel(String[] headers, Collection<T> dataset,
                            OutputStream out) {
        exportExcel("导出EXCEL文档", headers, null, dataset, out, "yyyy-MM-dd");
    }

    /**
     * 导出时，将导出所有的字段数据，但是headers会按照传入的数组导出
     *
     * @param headers
     * @param dataset
     * @param out
     * @param pattern
     */
    public void exportExcel(String[] headers, Collection<T> dataset,
                            OutputStream out, String pattern) {
        exportExcel("导出EXCEL文档", headers, null, dataset, out, pattern);
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title        表格标题名
     * @param headers      表格属性列名数组
     * @param exportFields 需要导出的对象的属性名，数据将按该数组的顺序输出
     * @param dataset      需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                     javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out          与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern      如果有时间数据，设定输出格式。默认为"yyyy-MM-dd"
     */
    @SuppressWarnings("unchecked")
    public void exportExcel(String title, String[] headers, String[] exportFields,
                            Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式，表头
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
//		font.setColor(XSSFColor);
//		font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式，正文
        XSSFCellStyle style2 = workbook.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        font2.setFontName("宋体");
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 产生表格标题行
        int startRowNum = 0;
        if (headers != null && headers.length > 0) {
            XSSFRow row = sheet.createRow(startRowNum);
            for (short i = 0; i < headers.length; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                XSSFRichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            startRowNum++;
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        while (it.hasNext()) {
            XSSFRow row = sheet.createRow(startRowNum);
            T       t   = (T) it.next();
            // 利用反射，根据 exportFields 属性的先后顺序，得到属性值
            if (exportFields != null && exportFields.length > 0) {//如果没有传入需要导出的字段，则导出全部字段
                for (short i = 0; i < exportFields.length; i++) {
                    XSSFCell cell = row.createCell(i);
                    cell.setCellStyle(style2);
                    try {
                        Class tCls  = t.getClass();
                        Field field = tCls.getDeclaredField(exportFields[i]);
                        field.setAccessible(true);
                        Object value = field.get(t);
                        // 判断值的类型后进行强制类型转换
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue    = (Boolean) value;
                            String  textValue = "是";
                            if (!bValue) {
                                textValue = "否";
                            }
                            cell.setCellValue(textValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            cell.setCellValue(date);
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            cell.setCellValue(value == null ? "" : value.toString());
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                Field[] fields = t.getClass().getFields();
                for (short i = 0; i < fields.length; i++) {
                    XSSFCell cell = row.createCell(i);
                    cell.setCellStyle(style2);
                    try {
                        Field field = fields[i];
                        field.setAccessible(true);
                        Object value = field.get(t);
                        // 判断值的类型后进行强制类型转换
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue    = (Boolean) value;
                            String  textValue = "是";
                            if (!bValue) {
                                textValue = "否";
                            }
                            cell.setCellValue(textValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            cell.setCellValue(date);
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            cell.setCellValue(value == null ? "" : value.toString());
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            startRowNum++;
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(ByteArrayOutputStream os, HttpServletResponse response, String fileName) {
        BufferedInputStream  bis = null;
        BufferedOutputStream bos = null;
        try {
            byte[]      content = os.toByteArray();
            InputStream is      = new ByteArrayInputStream(content);
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes("utf-8"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int    bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}