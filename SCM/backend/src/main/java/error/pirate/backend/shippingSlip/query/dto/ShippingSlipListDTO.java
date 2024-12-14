package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "출하전표 리스트 DTO")
public class ShippingSlipListDTO {
    private long shippingSlipSeq;    // 출하전표 번호
    private String shippingSlipName; // 출하전표명
    private LocalDateTime shippingSlipShippingDate; // 출하일
    private String clientName;  // 거래처명
    private String itemName;    // 품목명
}
