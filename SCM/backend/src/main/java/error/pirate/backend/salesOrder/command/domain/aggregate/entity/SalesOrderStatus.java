package error.pirate.backend.salesOrder.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum SalesOrderStatus {
    BEFORE("결재 전"),
    AFTER("결재 후"),
    PRODUCTION("생산 중"),
    PRODUCTION_COMPLETE("생산 완료"),
    SHIPMENT_COMPLETE("출하 완료"),
    DELETE("삭제");

    private final String value;

    @Getter
    public static class SalesOrderStatusResponse {
        private final String key;
        private final String value;

        public SalesOrderStatusResponse(String key, SalesOrderStatus salesOrderStatus) {
            this.key = key;
            this.value = salesOrderStatus.getValue();
        }
    }

    public static List<SalesOrderStatus.SalesOrderStatusResponse> readSalesOrderStatusList() {
        return Arrays.stream(SalesOrderStatus.class.getEnumConstants())
                .filter(key -> !key.equals(SalesOrderStatus.DELETE))
                .map(key ->
                        new SalesOrderStatus.SalesOrderStatusResponse(key.toString(), SalesOrderStatus.valueOf(key.toString())))
                .toList();
    }
}
