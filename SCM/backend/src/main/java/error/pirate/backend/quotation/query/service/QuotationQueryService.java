package error.pirate.backend.quotation.query.service;

import error.pirate.backend.quotation.query.dto.*;
import error.pirate.backend.quotation.query.mapper.QuotationMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotationQueryService {

    private final ModelMapper modelMapper;
    private final QuotationMapper quotationMapper;

    // 견적서 목록 조회
    public QuotationListResponse readQuotationList(
            Integer page, Integer size,
            LocalDate startDate, LocalDate endDate,
            String clientName, String quotationStatus) {

        // 견적서 목록 조회
        List<QuotationListItemDTO> quotationList = quotationMapper.selectQuotationList(
                (page - 1) * size, size, startDate, endDate, clientName, quotationStatus);

        // 견적서 목록 개수 조회
        int totalQuotation = quotationMapper.countQuotationList(startDate, endDate, clientName, quotationStatus);

        return new QuotationListResponse(
                quotationList,
                page,
                (int) Math.ceil((double) totalQuotation / size),
                totalQuotation
        );
    }

    // 견적서 상세 조회
    public QuotationResponse readQuotation(Long quotationSeq) {

        // 견적서 상세 조회
        QuotationResponse response = modelMapper.map(
                quotationMapper.selectQuotation(quotationSeq), QuotationResponse.class);

        // 견적서 품목 목록 조회
        response.setQuotationItem(quotationMapper.selectQuotationItem(quotationSeq));

        return response;
    }

    // 견적서 현황 조회
    public QuotationSituationResponse readQuotationSituation(
            LocalDate startDate, LocalDate endDate, String clientName) {

        // 견적서 현황 조회
        return new QuotationSituationResponse(quotationMapper.selectQuotationSituation(startDate, endDate, clientName));
    }
}
