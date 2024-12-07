package error.pirate.backend.quotation.query.mapper;

import error.pirate.backend.quotation.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface QuotationMapper {
    // 견적서 목록 조회
    List<QuotationListItemDTO> selectQuotationList(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName,
            @Param("quotationStatus") String quotationStatus);

    // 견적서 개수 목록 조회
    int countQuotationList(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName,
            @Param("quotationStatus") String quotationStatus);

    // 견적서 상세 조회
    QuotationDTO selectQuotation(
            @Param("quotationSeq") Long quotationSeq);

    // 견적서 상세 품목 조회
    List<QuotationItemDTO> selectQuotationItem(
            @Param("quotationSeq") Long quotationSeq);

    // 견적서 현황 조회
    List<QuotationSituationDTO> selectQuotationSituation(LocalDate startDate, LocalDate endDate, String clientName);
}
