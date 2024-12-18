package error.pirate.backend.shippingInstruction.command.domain.aggregate.entity;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ShippingInstructionStatus {
    BEFORE("결재 전"), // 결재전
    AFTER("결재 후"),  // 결재후
    DELETE("삭제");  // 삭제

    private final String value;

    @Data
    public static class ShippingInstructionStatusResponse {
        private String key;
        private String value;

        public ShippingInstructionStatusResponse(String key, ShippingInstructionStatus shippingInstructionStatus) {
            this.key = key;
            this.value = shippingInstructionStatus.getValue();
        }
    }

    public static List<ShippingInstructionStatus.ShippingInstructionStatusResponse> readShippingInstructionList() {
        return Arrays.stream(ShippingInstructionStatus.class.getEnumConstants())
                .filter(key -> !key.equals(ShippingInstructionStatus.DELETE))
                .map(key -> new ShippingInstructionStatus.ShippingInstructionStatusResponse(key.toString(), ShippingInstructionStatus.valueOf(key.toString()))).toList();
    }

    public static String statusValue(String status) {
        for (ShippingInstructionStatus s : values()) {
            if (s.name().equals(status)) {
                return s.value;
            }
        }
        throw new CustomException(ErrorCodeType.SHIPPING_INSTRUCTION_STATE_BAD_REQUEST);
    }
}
