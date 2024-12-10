package error.pirate.backend.purchaseOrder.query.service;

import error.pirate.backend.common.Pagination;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderRequest;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponse;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponsePagination;
import error.pirate.backend.purchaseOrder.query.mapper.PurchaseOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderResponsePagination readPurchaseOrderList(PurchaseOrderRequest purchaseOrderRequest) {
        List<PurchaseOrderResponse> purchaseOrderResponseList = purchaseOrderMapper.readPurchaseOrderList(purchaseOrderRequest);
        int totalCount = purchaseOrderMapper.readPurchaseOrderListCount(purchaseOrderRequest);

        Pagination pagination = new Pagination();
        pagination.responsePaging(purchaseOrderRequest.getPageNo(), totalCount);

        return PurchaseOrderResponsePagination.builder()
                .purchaseOrderResponseList(purchaseOrderResponseList)
                .pagination(pagination)
                .build();

    }

    public void purchaseOrderExcelDown(Long purchaseOrderSeq) {
    }

}
