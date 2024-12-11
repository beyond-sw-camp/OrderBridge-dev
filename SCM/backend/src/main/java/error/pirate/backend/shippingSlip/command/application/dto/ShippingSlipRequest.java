package error.pirate.backend.shippingSlip.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "출하전표 요청 데이터")
public class ShippingSlipRequest {
    private LocalDateTime shippingSlipShippingDate; // 출하일
    private long shippingInstructionSeq;  // 출하지시서 시퀀스
    private String shippingSlipNote; // 출하전표 비고
    private List<ShippingSlipItemDTO> shippingSlipItems; // 물품 리스트
}
