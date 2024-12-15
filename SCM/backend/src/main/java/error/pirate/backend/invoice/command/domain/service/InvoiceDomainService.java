package error.pirate.backend.invoice.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.invoice.query.dto.InvoiceItemCheckDTO;
import error.pirate.backend.invoice.query.service.InvoiceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceDomainService {

    private final InvoiceQueryService invoiceQueryService;

    // 견적서와 주문서의 품목 수량 비교
    public void validateItem(Long salesOrderSeq) {
        List<InvoiceItemCheckDTO> invoiceItemCheckList = invoiceQueryService.invoiceItemCheck(salesOrderSeq);

        for (InvoiceItemCheckDTO invoiceItemCheck : invoiceItemCheckList) {
            if (invoiceItemCheck.getRemainingQuantity() < 0) {
                throw new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_MATCH);
            }
        }
    }
}
