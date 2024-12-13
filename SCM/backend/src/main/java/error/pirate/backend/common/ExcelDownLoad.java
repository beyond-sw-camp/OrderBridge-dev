package error.pirate.backend.common;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Slf4j
@Component
public class ExcelDownLoad {

    public byte[] excelDownBody(String[][] excel, String[] header, String name) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(name + ".xlsx");

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
}
