package error.pirate.backend.shippingInstruction.command.application.dto;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "출하지시서 요청 데이터")
public class ShippingInstructionRequest {
    private LocalDateTime shippingInstructionScheduledShipmentDate; // 출하예정일
    private long salesOrderSeq;  // 주문서 시퀀스
    private ShippingInstructionAddress shippingInstructionAddress;  // 출하 주소
    private String shippingInstructionNote; // 출하 지시서 비고
    private List<ShippingInstructionItemRequest> shippingInstructionItems; // 물품 리스트
}
