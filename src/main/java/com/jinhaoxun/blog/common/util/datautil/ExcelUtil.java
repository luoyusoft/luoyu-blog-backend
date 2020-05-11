package com.jinhaoxun.blog.common.com.util.datautil;

import com.jinhaoxun.blog.common.com.exception.ExceptionFactory;
import com.jinhaoxun.blog.common.com.response.ResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-05-09
 * @description Excel工具类
 */
@Slf4j
public class ExcelUtil {

    /**
     * 自定义Excel文件类型
     */
    private final static String XLS_TYPE = ".xls";
    /**
     * 自定义Excel文件类型
     */
    private final static String XLSX_TYPE = ".xlsx";


    /**************************           解析Excel             *******************************/

    /**
     * @author jinhaoxun
     * @description 读取Excel文件方法
     * @param inputStream 文件流
     * @param exceptionFactory 异常工厂对象
     * @param fileName 文件名
     * @return List<String[]> 读取出来的Excel文件内容
     * @throws Exception
     */
    public static List<String[]> readExcel(InputStream inputStream, ExceptionFactory exceptionFactory, String fileName) throws Exception {
        //检查文件
        checkFile(fileName, exceptionFactory);
        Workbook workBook = getWorkbook(inputStream, fileName);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> valueList = new ArrayList<>();
        if (workBook != null) {
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workBook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //始止行
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++ ) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    int firstCellNum = row.getFirstCellNum();
                    int lastCellNum = row.getLastCellNum();
                    String[] cells = new String[lastCellNum];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String cellValue = getCellValue(cell,exceptionFactory);
                        cells[cellNum] = cellValue;
                    }
                    valueList.add(cells);
                }
            }
            workBook.close();
        }
        return valueList;
    }

    /**
     * @author jinhaoxun
     * @description 获取不同格式的单元格内容方法
     * @param cell 单元对象
     * @param exceptionFactory 异常工厂对象
     * @return String 获取到的单元格内容
     * @throws Exception
     */
    private static String getCellValue(Cell cell, ExceptionFactory exceptionFactory) throws Exception {
        String cellValue;
        if (cell == null) {
            return null;
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }
        //判断不同的数据类型
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                throw exceptionFactory.build(ResponseMsg.EXCEL_ILLEGAL_CHAR.getCode(), cell.getStringCellValue() + "单元格内容含有非法字符");
            default:
                throw exceptionFactory.build(ResponseMsg.EXCEL_UNKNOW_CHAR_TYPE.getCode(), cell.getStringCellValue() + "单元格内容是未知数据类型");
        }
        return cellValue;
    }

    /**
     * @author jinhaoxun
     * @description 获取不同后缀的excel文件方法
     * @param inputStream 文件流
     * @param fileName 文件名
     * @return Workbook 获取到的Excel文件
     * @throws IOException
     */
    private static Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        Workbook workbook = null;
        if (fileName.endsWith(XLS_TYPE)) {
            //excel 2003
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileName.endsWith(XLSX_TYPE)) {
            //excel 2007
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    /**
     * @author jinhaoxun
     * @description 判断文件是否为Excel方法
     * @param fileName 文件名
     * @param exceptionFactory 异常工厂对象
     * @throws Exception
     */
    private static void checkFile(String fileName, ExceptionFactory exceptionFactory) throws Exception {
        //获取文件名，判断是否是excel文件
        if (!fileName.endsWith(XLS_TYPE) && !fileName.endsWith(XLSX_TYPE)) {
            throw exceptionFactory.build(ResponseMsg.NO_EXCEL_FILE.getCode(), "不是Excle文件");
        }
    }

    /**************************           生成Excel             *******************************/

    /**
     * @author jinhaoxun
     * @description 创建Excel文档
     * @param list 录入的数据
     * @param keys 列字段名称集合
     * @param columnNames 列头文件名集合
     * @return HSSFWorkbook 生成的Excel文档
     */
        public static HSSFWorkbook createWorkBook(List<Map<String, Object>> list, String[] keys, String[] columnNames) {
        // 1. 创建excel工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        // 2. 创建第一个sheet页，并命名
        HSSFSheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 3. 设置每列的宽
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (50 * 60));
        }
        // 4. 创建第一行，设置其单元格格式，并将数据放入
        HSSFRow row = sheet.createRow((short) 0);
        row.setHeight((short) 500);
        // 4.1 设置单元格格式
        HSSFCellStyle cs = wb.createCellStyle();
        HSSFFont f = wb.createFont();
        f.setFontName("宋体");
        f.setFontHeightInPoints((short) 10);
        f.setBold(true);
        cs.setFont(f);
        // 水平居中
        cs.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setLocked(true);
        //自动换行
        cs.setWrapText(true);
        // 4.2 设置列名（取出列名集合进行创建）
        for (int i = 0; i < columnNames.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        // 5. 设置首行外,每行每列的值(Row和Cell都从0开始)
        for (short i = 1; i < list.size(); i++) {
            HSSFRow row1 = sheet.createRow(i);
            // 5.1 在Row行创建单元格
            for (short j = 0; j < keys.length; j++) {
                HSSFCell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
            }
            // 5.2 设置该行样式
            HSSFFont f2 = wb.createFont();
            f2.setFontName("宋体");
            f2.setFontHeightInPoints((short) 10);
            // 5.3 设置单元格样式
            HSSFCellStyle cs2 = wb.createCellStyle();
            cs2.setFont(f2);
            // 左右居中
            cs2.setAlignment(HorizontalAlignment.CENTER);
            // 上下居中
            cs2.setVerticalAlignment(VerticalAlignment.CENTER);
            cs2.setLocked(true);
            //自动换行
            cs2.setWrapText(true);
            for (int m = 0; m < keys.length; m++) {
                HSSFCell hssfCell = row1.getCell(m);
                hssfCell.setCellStyle(cs2);
            }
        }
        return wb;
    }
}
