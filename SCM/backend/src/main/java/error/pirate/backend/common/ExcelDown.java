package error.pirate.backend.common;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class ExcelDown {

    public static byte[] excelDownBody(List<ExcelDTO> excelList, String name, String[] headers) {
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

            for (int i=0 ; i< headers.length ; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            if(ObjectUtils.isNotEmpty(excelList)) {
                int rowNum = 1;
                for (ExcelDTO excel : excelList) {
                    Row row = sheet.createRow(rowNum++);

                    int colNum = 0;
                    row.createCell(colNum++).setCellValue(excel.getCell1());
                    row.createCell(colNum++).setCellValue(excel.getCell2());
                    row.createCell(colNum++).setCellValue(excel.getCell3());
                    row.createCell(colNum++).setCellValue(excel.getCell4());
                    row.createCell(colNum++).setCellValue(excel.getCell5());
                    row.createCell(colNum++).setCellValue(excel.getCell6());
                    row.createCell(colNum++).setCellValue(excel.getCell7());
                    row.createCell(colNum++).setCellValue(excel.getCell8());
                    row.createCell(colNum++).setCellValue(excel.getCell9());
                    row.createCell(colNum).setCellValue(excel.getCell10());
                }
            }

            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            log.error("excelDown Exception : ", e);

            throw new CustomException(ErrorCodeType.EXCEL_DOWN_ERROR);
        }
    }

    public static HttpHeaders excelDownHeader(String name) {
        HttpHeaders headersResponse = new HttpHeaders();
        String fileName = URLEncoder.encode(name + " (" + new SimpleDateFormat("yyyy/MM/dd_HH-mm").format(new Date()) + ").xlsx", StandardCharsets.UTF_8);
        headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return headersResponse;
    }
}
