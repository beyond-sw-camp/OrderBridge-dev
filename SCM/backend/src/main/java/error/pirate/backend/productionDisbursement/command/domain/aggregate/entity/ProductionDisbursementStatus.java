package error.pirate.backend.productionDisbursement.command.domain.aggregate.entity;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductionDisbursementStatus {
    BEFORE("불출 전"), // 불출전
    AFTER("불출 후"),   // 불출후
    DELETE("삭제");  // 삭제

    private final String value;

    @Data
    public static class ProductionDisbursementStatusResponse {
        private String key;
        private String value;

        public ProductionDisbursementStatusResponse(String key, ProductionDisbursementStatus productionDisbursementStatus) {
            this.key = key;
            this.value = productionDisbursementStatus.getValue();
        }
    }

    public static String statusValue(String status) {
        for(ProductionDisbursementStatus s : ProductionDisbursementStatus.values()) {
            if(s.name().equals(status)) {
                return s.value;
            }
        }
        throw new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_STATE_BAD_REQUEST);
    }
}
