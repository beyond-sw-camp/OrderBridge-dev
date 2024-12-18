package error.pirate.backend.purchase.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.common.Pagination;
import error.pirate.backend.purchase.query.dto.PurchaseItemResponse;
import error.pirate.backend.purchase.query.dto.PurchaseRequest;
import error.pirate.backend.purchase.query.dto.PurchaseResponse;
import error.pirate.backend.purchase.query.dto.PurchaseResponsePagination;
import error.pirate.backend.purchase.query.mapper.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseMapper purchaseMapper;

    private final ExcelDownLoad excelDownBody;

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

    public byte[] purchaseExcelDown(PurchaseRequest request) {
        request.setLimit(null);
        request.setOffset(null);
        List<PurchaseResponse> purchaseResponseList = purchaseMapper.readPurchaseList(request);

        String[] headers = {"구매서명", "구매서 품목", "거래처명", "계약일", "상태"};
        String[][] excel = new String[purchaseResponseList.size()][headers.length];

        for(int i=0 ; i<purchaseResponseList.size() ; i++) {
            PurchaseResponse dto = purchaseResponseList.get(i);
            dto.setPurchaseItemResponseList(purchaseMapper.readPurchaseItemList(dto.getPurchaseOrderSeq()));

            excel[i][0] = dto.getPurchaseName();
            excel[i][1] = dto.getPurchaseItemResponseList()
                    .stream()
                    .map(PurchaseItemResponse::getItemName)
                    .collect(Collectors.joining(", "));//  품목
            excel[i][2] = dto.getClientName();
            excel[i][3] = dto.getPurchaseContractDate() != null
                    ? dto.getPurchaseContractDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : null;
            excel[i][4] = String.valueOf(dto.getPurchaseStatus());
        }

        return excelDownBody.excelDownBody(excel, headers, "구매서");
    }

}
