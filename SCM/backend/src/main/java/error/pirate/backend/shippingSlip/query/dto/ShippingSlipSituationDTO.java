package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "출하전표 현황 DTO")
public class ShippingSlipSituationDTO {
    private long shippingSlipSeq;    // 출하전표 번호
    private LocalDate shippingSlipShippingDate; // 출하일
    private String shippingSlipName; // 출하전표명
    private int shippingSlipTotalQuantity;   // 출하전표 총수량
    private String clientName;  // 거래처명
    private String shippingSlipAddress;  // 출하전표 주소
    private String shippingSlipNote; // 출하전표 비고
}
