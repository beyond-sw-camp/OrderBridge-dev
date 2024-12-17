package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "출하전표 현황 응답")
public class ShippingSlipSituationResponse {
    private long shippingSlipSeq;    // 출하전표 번호
    private LocalDateTime shippingSlipShippingDate; // 출하일
    private String  shippingSlipShippingMonthDate; // 월별 출하일
    private String shippingSlipName; // 출하전표명
    private String shippingSlipTotalQuantity;   // 출하전표 총수량
    private String clientName;  // 거래처명
    private String shippingSlipAddress;  // 출하전표 주소
    private String shippingSlipNote; // 출하전표 비고
    private String shippingSlipTotalQuantitySum;  // 출하전표 총수량 월합계 총합계
}
