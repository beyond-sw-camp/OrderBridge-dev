package error.pirate.backend.workOrder.query.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "작업지시서 목록 필터링 DTO")
public class WorkOrderFilterDTO {

    private String warehouseName;          // 생산창고 이름
    private String workOrderStatus;        // 작업지시 상태

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate startDate;       // 검색 시작일
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate endDate;         // 검색 종료일

    private Integer page = 1;              // 기본값: 1
    private Integer size = 10;             // 기본값: 10

}
