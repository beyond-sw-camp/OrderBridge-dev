package error.pirate.backend.quotation.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum QuotationStatus {
    BEFORE("결재 전"),
    AFTER("결재 후"),
    REFUSAL("반려"),
    DELETE("삭제");

    private final String value;

    @Getter
    public static class QuotationStatusResponse {
        private final String key;
        private final String value;

        public QuotationStatusResponse(String key, QuotationStatus quotationStatus) {
            this.key = key;
            this.value = quotationStatus.getValue();
        }
    }

    public static List<QuotationStatusResponse> readQuotationStatusList() {
        return Arrays.stream(QuotationStatus.class.getEnumConstants()).map(key ->
                new QuotationStatusResponse(key.toString(), QuotationStatus.valueOf(key.toString()))).toList();
    }
}
