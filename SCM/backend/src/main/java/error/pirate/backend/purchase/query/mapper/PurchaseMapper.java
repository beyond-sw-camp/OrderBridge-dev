package error.pirate.backend.purchase.query.mapper;

import error.pirate.backend.purchase.query.dto.PurchaseItemResponse;
import error.pirate.backend.purchase.query.dto.PurchaseRequest;
import error.pirate.backend.purchase.query.dto.PurchaseResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseMapper {

    List<PurchaseResponse> readPurchaseList(PurchaseRequest purchaseRequest);

    int readPurchaseListCount(PurchaseRequest purchaseRequest);

    List<PurchaseItemResponse> readPurchaseItemList(Long purchaseSeq);

}
