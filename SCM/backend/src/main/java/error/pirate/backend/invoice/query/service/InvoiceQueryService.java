package error.pirate.backend.invoice.query.service;

import error.pirate.backend.invoice.command.domain.aggregate.entity.InvoiceStatus;
import error.pirate.backend.invoice.query.dto.*;
import error.pirate.backend.invoice.query.mapper.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceQueryService {

    private final InvoiceMapper invoiceMapper;
    private final ModelMapper modelMapper;

    // 거래 명세서 목록 조회
    public InvoiceListResponse readInvoiceList(
            Integer page, Integer size, LocalDate startDate, LocalDate endDate, String clientName) {

        // 거래 명세서 목록 조회
        List<InvoiceListItemDTO> invoiceList = invoiceMapper.selectInvoiceList(
                (page - 1) * size, size, startDate, endDate, clientName);

        int totalInvoice = invoiceMapper.countInvoiceList(startDate, endDate, clientName);

        return new InvoiceListResponse(invoiceList, page, (int) Math.ceil((double) totalInvoice / size), totalInvoice);
    }

    // 거래 명세서 상세 조회
    public InvoiceResponse readInvoice(Long invoiceSeq) {

        // 거래 명세서 상세 조회
        InvoiceResponse response = modelMapper.map(
                invoiceMapper.selectInvoice(invoiceSeq), InvoiceResponse.class);

        // 거래 명세서 품목 목록 조회
        response.setInvoiceItem(invoiceMapper.selectInvoiceItem(invoiceSeq));

        return response;
    }

    // 거래 명세서 현황 조회
    public InvoiceSituationResponse readInvoiceSituation(LocalDate startDate, LocalDate endDate, String clientName) {

        // 거래 명세서 현황 조회
        return new InvoiceSituationResponse(invoiceMapper.selectInvoiceSituation(startDate, endDate, clientName));
    }

    // 거래 명세서 값 확인
    public List<InvoiceItemCheckDTO> invoiceItemCheck(Long salesOrderSeq) {
        return invoiceMapper.sumInvoiceItemValue(salesOrderSeq);
    }
}
