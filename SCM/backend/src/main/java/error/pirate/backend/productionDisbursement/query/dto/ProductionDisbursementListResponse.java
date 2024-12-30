package error.pirate.backend.productionDisbursement.query.dto;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "생산불출 목록 응답 DTO")
public class ProductionDisbursementListResponse {

    private List<ProductionDisbursementListDTO> productionDisbursementList; // 불출 목록
    private List<ProductionDisbursementStatus.ProductionDisbursementStatusResponse> productionDisbursementStatusList;  // 상태목록
    private int currentPage;                     // 현재 페이지
    private int totalPages;                      // 전체 페이지 수
    private long totalItems;                     // 총 작업지시서 수


}
