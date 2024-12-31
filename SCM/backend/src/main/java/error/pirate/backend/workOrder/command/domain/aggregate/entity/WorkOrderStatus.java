package error.pirate.backend.workOrder.command.domain.aggregate.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum WorkOrderStatus {
    BEFORE("결재 전"), // 결재전
    AFTER("결재 후"), // 결재후
    ONGOING("생산 진행중"), // 생산 진행중
    COMPLETE("생산완료"), // 생산 완료
    STOP("중단"),    // 중단
    DELETE("삭제");  // 삭제

    private final String value;

    @Data
    public static class  WorkOrderStatusResponse {
        private String key;
        private String value;

        public WorkOrderStatusResponse(String key, WorkOrderStatus workOrderStatus) {
            this.key = key;
            this.value = workOrderStatus.getValue();
        }
    }

    public static List<WorkOrderStatus.WorkOrderStatusResponse> readWorkOrderStatusList() {
        return Arrays.stream(WorkOrderStatus.class.getEnumConstants())
                .filter(key -> !key.equals(WorkOrderStatus.DELETE))
                .map(key ->
                        new WorkOrderStatus.WorkOrderStatusResponse(key.toString(), WorkOrderStatus.valueOf(key.toString())))
                .toList();
    }


}
