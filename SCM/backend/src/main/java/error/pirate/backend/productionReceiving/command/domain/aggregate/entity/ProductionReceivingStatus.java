package error.pirate.backend.productionReceiving.command.domain.aggregate.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductionReceivingStatus {
    BEFORE("결재 전"), // 결재전
    AFTER("결재 후"),  // 결재후
    COMPLETE("생산입고 완료"), // 생산입고 완료
    DELETE("삭제"); // 삭제

    private final String value;

    @Data
    public static class ProductionReceivingStatusResponse {
        private String key;
        private String value;

        public ProductionReceivingStatusResponse(String key, ProductionReceivingStatus productionReceivingStatus) {
            this.key = key;
            this.value = productionReceivingStatus.getValue();
        }
    }
}
