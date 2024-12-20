package error.pirate.backend.workOrder.command.domain.aggregate.entity;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    public static String statusValue(String status) {
        for (WorkOrderStatus s : WorkOrderStatus.values()) {
            if(s.name().equals(status)) {
                return s.value;
            }
        }
        throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
    }



}
