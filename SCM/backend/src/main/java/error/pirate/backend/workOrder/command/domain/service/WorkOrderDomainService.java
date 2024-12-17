package error.pirate.backend.workOrder.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.service.BomItemDomainService;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
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

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderDomainService {

    private final WorkOrderRepository workOrderRepository;
    private final ItemInventoryDomainService itemInventoryDomainService;
    private final BomItemDomainService bomItemDomainService;

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

    // 중복 및 BOM 검증 로직
    public void validateAndCheckForDuplicates(SalesOrder salesOrder, SalesOrderItem salesOrderItem, Integer workOrderIndicatedQuantity) {
        if (existsBySalesOrderAndItem(salesOrder.getSalesOrderSeq(), salesOrderItem.getItem().getItemSeq())) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_DUPLICATE);
        }
        log.info("중복 작업지시서 확인 완료 - 주문서 번호: {}, 품목 번호: {}", salesOrder.getSalesOrderSeq(), salesOrderItem.getItem().getItemSeq());

        // BOM 품목 및 재고 검증
        List<BomItem> bomItems = bomItemDomainService.findAllByParentItem(salesOrderItem.getItem());
        itemInventoryDomainService.checkInventoryForBomItems(bomItems, workOrderIndicatedQuantity);
        log.info("BOM 및 재고 검증 완료 - 품목 번호: {}", salesOrderItem.getItem().getItemSeq());
    }

    // 중복체크
    private boolean existsBySalesOrderAndItem(Long salesOrderSeq, Long itemSeq) {
        return workOrderRepository.existsBySalesOrderSeqAndItemSeq(salesOrderSeq, itemSeq);
    }

    // 작업지시서 번호로 작업지시서 조회
    public WorkOrder findByWorkOrderSeq(Long workOrderSeq) {
        return workOrderRepository.findById(workOrderSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.WORK_ORDER_NOT_FOUND));
    }

       // 작업지시일 시간 처리
    public LocalDateTime convertIndicatedDate(LocalDate indicatedDate) {
        LocalDateTime regDate = LocalDateTime.now();

        if (indicatedDate.isEqual(regDate.toLocalDate())) {
            // 등록일과 같으면 등록 시간 + 1시간
            return regDate.plusHours(1);
        } else {
            // 등록일과 다르면 오전 9시로 설정
            return indicatedDate.atTime(LocalTime.of(9, 0));
        }
    }

    // 납기일 시간 처리
    public LocalDateTime convertDueDate(LocalDate dueDate) {
        return dueDate.atTime(LocalTime.of(23, 59, 59));
    }

    // 작업지시서 수정
    public void updateWorkOrder(WorkOrder workOrder, SalesOrder salesOrder, SalesOrderItem salesOrderItem, Warehouse warehouse,
                                LocalDateTime workOrderIndicatedDate, LocalDateTime workOrderDueDate,
                                int workOrderIndicatedQuantity, String workOrderNote) {
        workOrder.updateWorkOrder(
                salesOrder,
                salesOrderItem,
                warehouse,
                workOrderIndicatedDate,
                workOrderDueDate,
                workOrderIndicatedQuantity,
                workOrderNote
        );
    }

    // 작업지시서 삭제(상태로 변경)
    public void deleteWorkOrder(WorkOrder workOrder) {

        // 상태 수정을 위해 엔터티 변경
        workOrder.deleteWorkOrder();
    }

    // 작업지시서 삭제 가능한 상태 확인
    public void checkWorkOrderStatusDeletePossible(WorkOrderStatus workOrderStatus) {
        if (!(workOrderStatus.equals(WorkOrderStatus.BEFORE) || workOrderStatus.equals(WorkOrderStatus.STOP))) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
        }
    }

    // 작업지시서 결재 상태 확인
    public void checkWorkOrderStatusApproval(WorkOrderStatus workOrderStatus) {
        /* 결재전이 아니라면 변경 불가*/
        if (!workOrderStatus.equals(WorkOrderStatus.BEFORE)) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
        }
    }

    // 작업지시서 상태 변경
    public void updateWorkOrderStatus(WorkOrder workOrder, WorkOrderStatus newStatus) {
        workOrder.changeWorkOrderStatus(newStatus);
    }

    // 완료 상태시 업데이트
    public void updateWorkOrderWorkAutoComplete(WorkOrder workOrder, Integer indicatedQuantity) {
        workOrder.updateWorkOrderWorkAutoComplete(indicatedQuantity);
    }
}
