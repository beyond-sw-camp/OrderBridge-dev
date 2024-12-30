package error.pirate.backend.common;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Slf4j
@Component
public class ExcelDownLoad {

    public byte[] excelDownBody(String[][] excel, String[] header, String name) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(name);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 헤더 행 생성
            Row headerRow = sheet.createRow(0);

            for (int i = 0 ; i < header.length ; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < excel.length; i++) {
                Row row = sheet.createRow(i+1);

                for(int j = 0; j < excel[i].length; j++) {
                    row.createCell(j).setCellValue(excel[i][j]);
                }
            }
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            log.error("excelDown Exception : ", e);

            throw new CustomException(ErrorCodeType.EXCEL_DOWN_ERROR);
        }
    }

    public byte[] writeCells(String[] headers, ArrayList<Object> list) {
        String[][] cells = new String[list.size()][headers.length];

        int status = 0;
        int address = 0;
        for(int i = 0; i < headers.length; i++) {
            if(headers[i].contains("주소")) {
                address = i;
            }
            if (headers[i].contains("상태")){
                status = i;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            Field[] fields = list.get(i).getClass().getDeclaredFields();
            for (int j = 0; j < headers.length; j++) {
                Field field = fields[j];

                String fieldName = field.getName();
                String getterMethodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                try {
                    Method getterMethod = list.get(i).getClass().getMethod(getterMethodName);
                    Object value = getterMethod.invoke(list.get(i));

                    String stringValue;
                    if (value == null) {
                        stringValue = "";
                    } else if (value instanceof Enum) {
                        // Enum인 경우 getValue 메서드 호출
                        Method valueMethod = value.getClass().getMethod("getValue");
                        stringValue = (String) valueMethod.invoke(value);
                    } else {
                        stringValue = value.toString();
                    }

                    cells[i][j] = stringValue;
                } catch (Exception e) {
                    log.error("writeCells Exception: ", e);
                }
            }
        }

        return excelDownBody(cells, headers, "Sheet");
    }
}
