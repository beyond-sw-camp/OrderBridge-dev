package error.pirate.backend.workOrder.query.dto;

import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "작업지시서 목록 응답 DTO")
public class WorkOrderListResponse {

    private List<WorkOrderListDTO> workOrderList; // 작업지시서 목록
    private List<WorkOrderStatus.WorkOrderStatusResponse> workOrderStatusList;  // 상태목록
    private int currentPage;                     // 현재 페이지
    private int totalPages;                      // 전체 페이지 수
    private long totalItems;                     // 총 작업지시서 수

}
