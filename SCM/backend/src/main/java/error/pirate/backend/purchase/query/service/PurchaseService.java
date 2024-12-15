package error.pirate.backend.purchase.query.service;

import error.pirate.backend.common.Pagination;
import error.pirate.backend.purchase.query.dto.PurchaseItemResponse;
import error.pirate.backend.purchase.query.dto.PurchaseRequest;
import error.pirate.backend.purchase.query.dto.PurchaseResponse;
import error.pirate.backend.purchase.query.dto.PurchaseResponsePagination;
import error.pirate.backend.purchase.query.mapper.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseMapper purchaseMapper;

    public PurchaseResponsePagination readPurchaseList(PurchaseRequest request) {
        List<PurchaseResponse> purchaseResponseList = purchaseMapper.readPurchaseList(request);
        for (PurchaseResponse purchaseResponse : purchaseResponseList) {
            List<PurchaseItemResponse> purchaseItemResponseList = purchaseMapper.readPurchaseItemList(purchaseResponse.getPurchaseSeq());
            purchaseResponse.setPurchaseItemResponseList(purchaseItemResponseList);
        }

        int totalCount = purchaseMapper.readPurchaseListCount(request);

        Pagination pagination = new Pagination();
        pagination.responsePaging(request.getPageNo(), totalCount);

        return PurchaseResponsePagination.builder()
                .purchaseResponseList(purchaseResponseList)
                .pagination(pagination)
                .build();

    }

}
