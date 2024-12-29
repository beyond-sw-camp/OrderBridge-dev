package error.pirate.backend.productionDisbursement.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "생산불출 상세 응답 DTO")
public class ProductionDisbursementResponse {

    private ProductionDisbursementDetailDTO productionDisbursementDetail;
    private List<ProductionDisbursementItemDTO> itemList;

}
