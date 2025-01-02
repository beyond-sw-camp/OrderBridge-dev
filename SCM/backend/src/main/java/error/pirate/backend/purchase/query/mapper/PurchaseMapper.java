package error.pirate.backend.purchase.query.mapper;

import error.pirate.backend.purchase.query.dto.PurchaseItemResponse;
import error.pirate.backend.purchase.query.dto.PurchaseRequest;
import error.pirate.backend.purchase.query.dto.PurchaseResponse;
import error.pirate.backend.purchase.query.dto.PurchaseSituationResponse;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseMapper {

    List<PurchaseResponse> readPurchaseList(PurchaseRequest purchaseRequest);

    int readPurchaseListCount(PurchaseRequest purchaseRequest);

    List<PurchaseItemResponse> readPurchaseItemList(Long purchaseSeq);

    List<PurchaseSituationResponse> readPurchaseOrderSituationList(PurchaseRequest request);

    PurchaseResponse readPurchase(Long purchaseSeq);
}
