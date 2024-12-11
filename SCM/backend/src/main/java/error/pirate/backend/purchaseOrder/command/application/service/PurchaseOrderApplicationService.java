package error.pirate.backend.purchaseOrder.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;
import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderDomainService;
import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderItemDomainService;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderApplicationService {

    private final PurchaseOrderDomainService purchaseOrderDomainService;

    private final PurchaseOrderItemDomainService purchaseOrderItemDomainService;

    private final SalesOrderDomainService salesOrderDomainService;

    @Transactional
    public void createPurchaseOrder(PurchaseOrderCreateRequest request) {
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
        if(salesOrder.getSalesOrderStatus() == SalesOrderStatus.AFTER) {
            PurchaseOrder purchaseOrder = purchaseOrderDomainService.createPurchaseOrder(request);
            purchaseOrderItemDomainService.createPurchaseOrderItem(purchaseOrder.getPurchaseOrderSeq(), request);
        } else {
            throw new CustomException(ErrorCodeType.SALES_ORDER_STATE_BAD_REQUEST);
        }
    }

    @Transactional
    public void updatePurchaseOrder(PurchaseOrderUpdateRequest request) {
        purchaseOrderDomainService.updatePurchaseOrder(request);
        purchaseOrderItemDomainService.updatePurchaseOrderItem(request);
    }

    @Transactional
    public void deletePurchaseOrder(Long purchaseOrderSeq, Long salesOrderSeq) {
        SalesOrder salesOrder = salesOrderDomainService.findById(salesOrderSeq);
        // 생산 중이 아닐 때만 삭제 가능
        if(salesOrder.getSalesOrderStatus() != SalesOrderStatus.PRODUCTION) {
            purchaseOrderDomainService.updatePurchaseStatus(purchaseOrderSeq, PurchaseOrderStatus.DELETE);
        } else {
            throw new CustomException(ErrorCodeType.SALES_ORDER_STATE_BAD_REQUEST);
        }
    }

    public void updatePurchaseOrderComplete(Long purchaseOrderSeq) {
        purchaseOrderDomainService.updatePurchaseStatus(purchaseOrderSeq, PurchaseOrderStatus.APPROVAL_AFTER);
    }

    public void updatePurchaseOrderRefusal(Long purchaseOrderSeq) {
        purchaseOrderDomainService.updatePurchaseStatus(purchaseOrderSeq, PurchaseOrderStatus.APPROVAL_REFUSAL);
    }

    public ResponseEntity<byte[]> purchaseOrderExcelDown() {
        List<PurchaseOrder> purchaseOrderList = purchaseOrderDomainService.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("발주서.xlsx");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 헤더 행 생성
            Row headerRow = sheet.createRow(0);
            String[] headers = {"발주서명", "계약 납기일", "목표 납기일"}; // TODO 아영 - 추가하기
            for (int i=0 ; i<headers.length ; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            if(ObjectUtils.isNotEmpty(purchaseOrderList)) {
                int rowNum = 1;
                for (PurchaseOrder order : purchaseOrderList) {
                    Row row = sheet.createRow(rowNum++);
                    for (int colNum=0 ; colNum<purchaseOrderList.size() ; colNum++) {
                        row.createCell(colNum++).setCellValue(order.getPurchaseOrderName());
                        row.createCell(colNum++).setCellValue(order.getPurchaseOrderDueDate());
                        row.createCell(colNum++).setCellValue(order.getPurchaseOrderTargetDueDate());
                    }
                }
            }

            workbook.write(out);

            HttpHeaders headersResponse = new HttpHeaders();
            String fileName = URLEncoder.encode("발주서[" + new SimpleDateFormat("yyyy/MM/dd_HH-mm").format(new Date()) + "].xlsx", StandardCharsets.UTF_8);
            headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headersResponse)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());

        } catch (Exception e) {
            throw new CustomException(ErrorCodeType.EXCEL_DOWN_ERROR);
        }
    }

}
