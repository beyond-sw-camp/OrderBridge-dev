package error.pirate.backend.workOrder.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderDomainService {

    private final WorkOrderRepository workOrderRepository;

    // 작업지시서 생성
    public WorkOrder createWorkOrder(CreateWorkOrderRequest request, SalesOrder salesOrder, SalesOrderItem salesOrderItem, Warehouse warehouse, User user,
                                     LocalDateTime seoulDueDate, LocalDateTime seoulIndicatedDate) {
        return WorkOrder.createWorkOrder(request, salesOrder, salesOrderItem, warehouse, user, seoulIndicatedDate, seoulDueDate);
    }

    // 작업지시서 저장
    public void saveWorkOrder(WorkOrder workOrder) {
        workOrderRepository.save(workOrder);
    }

    // 작업지시일 시간대 변경
    public LocalDateTime setIndicatedDate(LocalDateTime workOrderIndicatedDate) {

        ZonedDateTime systemZonedDateTime = workOrderIndicatedDate.atZone(ZoneId.systemDefault());
        ZonedDateTime seoulZonedDateTime = systemZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return seoulZonedDateTime.toLocalDateTime();
    }

    // 만료일 시간대 변경
    public LocalDateTime setDueDate(LocalDateTime workOrderDueDate) {
        ZonedDateTime systemZonedDateTime = workOrderDueDate.atZone(ZoneId.systemDefault());
        ZonedDateTime seoulZonedDateTime = systemZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return seoulZonedDateTime.toLocalDateTime();
    }

    // 중복체크
    public boolean existsBySalesOrderAndItem(Long salesOrderSeq, Long itemSeq) {
        return workOrderRepository.existsBySalesOrderSeqAndItemSeq(salesOrderSeq, itemSeq);
    }

    // 작업지시서 번호로 작업지시서 조회
    public WorkOrder findByWorkOrderSeq(Long workOrderSeq) {
        return workOrderRepository.findById(workOrderSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.WORK_ORDER_NOT_FOUND));
    }

    // 작업지시서 상태 확인(결재 전인지)
    public void checkWorkOrderStatus(WorkOrderStatus workOrderStatus) {
        /* 결재전이 아니라면 변경 불가*/
        if (!workOrderStatus.equals(WorkOrderStatus.BEFORE)) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
        }
    }

    // 작업지시서 삭제(상태로 변경)
    public void deleteWorkOrder(WorkOrder workOrder) {

        /* 수정을 위해 엔터티 정보 변경 */
        workOrder.deleteWorkOrder();
    }
}
