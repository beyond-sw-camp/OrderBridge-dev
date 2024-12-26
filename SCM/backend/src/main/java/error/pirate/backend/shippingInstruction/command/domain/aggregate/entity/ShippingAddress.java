package error.pirate.backend.shippingInstruction.command.domain.aggregate.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ShippingAddress {
    GATE_1("인천공항 1번 게이트"),
    GATE_2("인천공항 2번 게이트"),
    GATE_3("인천공항 3번 게이트"),
    GATE_4("인천공항 4번 게이트"),
    GATE_5("인천공항 5번 게이트"),
    GATE_6("인천공항 6번 게이트"),
    GATE_7("인천공항 7번 게이트"),
    GATE_8("인천공항 8번 게이트"),
    GATE_9("인천공항 9번 게이트"),
    GATE_10("인천공항 10번 게이트");

    private final String value;

    @Data
    public static class ShippingInstructionAddressResponse {
        private String key;
        private String value;

        public ShippingInstructionAddressResponse(String key, ShippingAddress shippingAddress) {
            this.key = key;
            this.value = shippingAddress.getValue();
        }
    }

    public static List<ShippingAddress.ShippingInstructionAddressResponse> readShippingInstructionAddressList() {
        return Arrays.stream(ShippingAddress.class.getEnumConstants())
                .map(key -> new ShippingAddress.ShippingInstructionAddressResponse(key.toString(), ShippingAddress.valueOf(key.toString()))).toList();
    }
}
