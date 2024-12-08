package error.pirate.backend.shippingInstruction.query.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ShippingInstructionListRequest {

    private int page = 1; // 기본값 설정
    private int size = 10; // 기본값 설정

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // LocalDate 매핑을 위해 필요
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private String clientName;
    private String shippingInstructionStatus;
}
