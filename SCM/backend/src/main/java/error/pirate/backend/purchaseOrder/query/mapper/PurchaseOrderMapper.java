package error.pirate.backend.purchaseOrder.query.mapper;

import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderItemResponse;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderRequest;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponse;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderSituationResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseOrderMapper {

    List<PurchaseOrderResponse> readPurchaseOrderList(PurchaseOrderRequest purchaseOrderRequest);

    int readPurchaseOrderListCount(PurchaseOrderRequest purchaseOrderRequest);

    List<PurchaseOrderItemResponse> readPurchaseOrderItemList(Long purchaseOrderSeq);

    List<PurchaseOrderSituationResponse> readPurchaseOrderSituationList(PurchaseOrderRequest request);
}
