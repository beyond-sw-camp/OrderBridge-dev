package error.pirate.backend.shippingSlip.query.dto;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "출하전표 DTO")
public class ShippingSlipDTO {
    private long shippingSlipSeq;    // 출하전표 번호
    private String shippingSlipName; // 출하전표명
    private LocalDateTime shippingSlipShippingDate; // 출하일
    private String clientName;  // 거래처명
    private int shippingSlipTotalQuantity;   // 출하전표 총수량
    private ShippingAddress shippingAddress;  // 출하전표 주소
    private String userName;    // 출하전표 담당자
    private String shippingSlipNote; // 출하전표 비고
}
