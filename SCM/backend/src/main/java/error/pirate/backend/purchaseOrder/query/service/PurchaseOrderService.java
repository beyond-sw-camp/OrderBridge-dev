package error.pirate.backend.purchaseOrder.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.common.Pagination;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderItemResponse;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderRequest;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponse;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponsePagination;
import error.pirate.backend.purchaseOrder.query.mapper.PurchaseOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderMapper purchaseOrderMapper;

    private final ExcelDownLoad excelDownBody;

    public PurchaseOrderResponsePagination readPurchaseOrderList(PurchaseOrderRequest purchaseOrderRequest) {
        List<PurchaseOrderResponse> purchaseOrderResponseList = purchaseOrderMapper.readPurchaseOrderList(purchaseOrderRequest);
        for (PurchaseOrderResponse purchaseOrderResponse : purchaseOrderResponseList) {
            List<PurchaseOrderItemResponse> purchaseOrderItemResponseList = purchaseOrderMapper.readPurchaseOrderItemList(purchaseOrderResponse.getPurchaseOrderSeq());
            purchaseOrderResponse.setPurchaseOrderItemResponseList(purchaseOrderItemResponseList);
        }

        int totalCount = purchaseOrderMapper.readPurchaseOrderListCount(purchaseOrderRequest);

        Pagination pagination = new Pagination();
        pagination.responsePaging(purchaseOrderRequest.getPageNo(), totalCount);

        return PurchaseOrderResponsePagination.builder()
                .purchaseOrderResponseList(purchaseOrderResponseList)
                .pagination(pagination)
                .build();

    }

    public byte[] purchaseOrderExcelDown(PurchaseOrderRequest request) {
        request.setLimit(null);
        request.setOffset(null);
        List<PurchaseOrderResponse> purchaseOrderResponseList = purchaseOrderMapper.readPurchaseOrderList(request);

        String[] headers = {"발주서명", "발주서 품목", "거래처명", "계약 납기일", "목표 납기일", "상태"};
        String[][] excel = new String[purchaseOrderResponseList.size()][headers.length];

        for(int i=0 ; i<purchaseOrderResponseList.size() ; i++) {
            PurchaseOrderResponse dto = purchaseOrderResponseList.get(i);
            dto.setPurchaseOrderItemResponseList(purchaseOrderMapper.readPurchaseOrderItemList(dto.getPurchaseOrderSeq()));

            excel[i][0] = dto.getPurchaseOrderName();
            excel[i][1] = dto.getPurchaseOrderItemResponseList()
                    .stream()
                    .map(PurchaseOrderItemResponse::getItemName)
                    .collect(Collectors.joining(", "));//  품목
            excel[i][2] = dto.getClientName();
            excel[i][3] = dto.getPurchaseOrderDueDate() != null
                    ? dto.getPurchaseOrderDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : null;
            excel[i][4] = dto.getPurchaseOrderTargetDueDate() != null
                    ? dto.getPurchaseOrderTargetDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : null;
            excel[i][5] = String.valueOf(dto.getPurchaseOrderStatus());
        }

        return excelDownBody.excelDownBody(excel, headers, "발주서");
    }

}
