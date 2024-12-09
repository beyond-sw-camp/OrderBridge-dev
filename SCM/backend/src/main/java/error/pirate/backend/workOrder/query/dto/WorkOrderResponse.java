package error.pirate.backend.workOrder.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "작업지시서 상세 응답 DTO")
public class WorkOrderResponse {

    private WorkOrderDetailDTO workOrderDetail;
    private WorkOrderItemDTO workOrderItem;
}
