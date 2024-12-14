package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "출하전표 리스트 요청")
public class ShippingSlipListRequest {
    private Integer page = 1; // 기본값 설정
    private Integer size = 10; // 기본값 설정
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // LocalDate 매핑을 위해 필요
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private String clientName;
}
