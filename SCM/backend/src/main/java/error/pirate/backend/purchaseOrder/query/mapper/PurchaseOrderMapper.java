package error.pirate.backend.purchaseOrder.query.mapper;

import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderRequest;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseOrderMapper {

    List<PurchaseOrderResponse> readPurchaseOrderList(PurchaseOrderRequest purchaseOrderRequest);

    int readPurchaseOrderListCount(PurchaseOrderRequest purchaseOrderRequest);

}
