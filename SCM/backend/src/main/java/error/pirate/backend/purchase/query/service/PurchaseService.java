package error.pirate.backend.purchase.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.common.Pagination;
import error.pirate.backend.purchase.query.dto.*;
import error.pirate.backend.purchase.query.mapper.PurchaseMapper;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderItemResponse;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponse;
import io.micrometer.common.util.StringUtils;
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

        purchaseResponseList.forEach(order -> order.setPurchaseStatusValue(order.getPurchaseStatus().getValue()));
        
        Pagination pagination = new Pagination();
        pagination.responsePaging(request.getPageNo(), totalCount);

        return PurchaseResponsePagination.builder()
                .purchaseResponseList(purchaseResponseList)
                .pagination(pagination)
                .build();

    }

    public PurchaseResponse readPurchase(Long purchaseSeq) {
        PurchaseResponse purchaseResponse = purchaseMapper.readPurchase(purchaseSeq);
        List<PurchaseItemResponse> purchaseItemResponseList = purchaseMapper.readPurchaseItemList(purchaseResponse.getPurchaseSeq());
        purchaseResponse.setPurchaseItemResponseList(purchaseItemResponseList);

        return purchaseResponse;
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

    public List<PurchaseSituationResponse> readPurchaseOrderSituationList(PurchaseRequest request) {
        return purchaseMapper.readPurchaseOrderSituationList(request);
    }

    public byte[] purchaseOrderSituationExcelDown(PurchaseRequest request) {
        request.setLimit(null);
        request.setOffset(null);
        List<PurchaseSituationResponse> purchaseOrderResponseList = purchaseMapper.readPurchaseOrderSituationList(request);

        String[] headers = {"구매일자", "구매서명", "총 수량", "금액", "구매 계약일", "비고"};
        String[][] excel = new String[purchaseOrderResponseList.size()][headers.length];

        for (int i = 0; i < purchaseOrderResponseList.size(); i++) {
            PurchaseSituationResponse dto = purchaseOrderResponseList.get(i);

            if (dto.getPurchaseRegDate() != null) {
                excel[i][0] = dto.getPurchaseRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                excel[i][1] = dto.getPurchaseName();
                excel[i][2] = (dto.getPurchaseTotalQuantity() != null ? dto.getPurchaseTotalQuantity() : "0") + " 개";
                excel[i][3] = dto.getPurchaseExtendedPrice() + " 원";
                excel[i][4] = dto.getPurchaseContractDate() != null
                        ? dto.getPurchaseContractDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : null;
                excel[i][5] = dto.getPurchaseNote();
            } else {
                if (StringUtils.isNotEmpty(dto.getPurchaseRegMonth())) {
                    excel[i][0] = dto.getPurchaseRegMonth();
                    excel[i][1] = "-";
                    excel[i][2] = dto.getPurchaseMonthQuantity() + " 개";
                    excel[i][3] = dto.getPurchaseMonthPrice() + " 원";
                    excel[i][4] = "-";
                    excel[i][5] = "-";
                }
            }
        }
        return excelDownBody.excelDownBody(excel, headers, "구매 현황");
    }

}
