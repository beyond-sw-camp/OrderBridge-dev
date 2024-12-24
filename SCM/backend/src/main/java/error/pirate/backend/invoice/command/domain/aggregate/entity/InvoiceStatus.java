package error.pirate.backend.invoice.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum InvoiceStatus {
    CREATED("등록"),
    DELETE("삭제");
    
    private final String value;
    @Getter
    public static class InvoiceStatusResponse {
        private final String key;
        private final String value;

        public InvoiceStatusResponse(String key, InvoiceStatus invoiceStatus) {
            this.key = key;
            this.value = invoiceStatus.getValue();
        }
    }

    public static List<InvoiceStatus.InvoiceStatusResponse> readInvoiceStatusList() {
        return Arrays.stream(InvoiceStatus.class.getEnumConstants())
                .filter(key -> !key.equals(InvoiceStatus.DELETE))
                .map(key ->
                        new InvoiceStatus.InvoiceStatusResponse(key.toString(), InvoiceStatus.valueOf(key.toString())))
                .toList();
    }
}
