package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.common.NullCheck;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderItemDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.service.WarehouseDomainService;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.application.dto.UpdateWorkOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import error.pirate.backend.workOrder.command.domain.service.WorkOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderService {

    private final WorkOrderDomainService workOrderDomainService;
    private final SalesOrderDomainService salesOrderDomainService;
    private final SalesOrderItemDomainService salesOrderItemDomainService;
    private final WarehouseDomainService warehouseDomainService;
    private final NameGenerator nameGenerator;

    /* 작업지시서 등록 */
    @Transactional
    public void createWorkOrderForItem(CreateWorkOrderRequest request) {
        log.info("-------------- 작업지시서 등록 서비스 진입 :등록요청 조건 - request: {} --------------", request);

        // 주문서 상태 확인
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
        // 주문서의 결재상태 확인
        salesOrderDomainService.checkSalesOrderStatus(salesOrder.getSalesOrderStatus());

        // 주문서 품목 가져오기
        SalesOrderItem salesOrderItem = salesOrderItemDomainService.findBySalesOrderItemSeq(request.getSalesOrderItemSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_FOUND));

        Long itemSeq = salesOrderItem.getItem().getItemSeq();
        log.info("-------------- 찾은 ItemSeq: {}--------------", itemSeq);

        // 생산공장 확인
        Warehouse warehouse = warehouseDomainService.findById(request.getWarehouseSeq());

        // 사용자 설정 - 주문서 작성 유저
        User user = salesOrder.getUser();

        // 작업지시서명 설정
        String workOrderName = nameGenerator.nameGenerator(WorkOrder.class);
        log.info("Generated WorkOrderName: {}", workOrderName);

        // 주문서 품목 수량 확인
        int orderItemQuantity = salesOrderItem.getSalesOrderItemQuantity();
        log.info("SalesOrderItem Quantity: {}", orderItemQuantity);

        // workOrderIndicatedQuantity를 주문서 품목 수량으로 설정
        int workOrderIndicatedQuantity = orderItemQuantity;

        // 유효성 검사
        if (workOrderIndicatedQuantity <= 0) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_INVALID_QUANTITY);
        }

        // 주문서번호와 품목번호로(같은 주문서에서는 같은 품목 주문이 여러 개 들어오지 않는다는 가정) 중복체크 및 BOM 재고 검증
        workOrderDomainService.validateAndCheckForDuplicates(salesOrder, salesOrderItem, workOrderIndicatedQuantity);

        // 작업지시일 시간대 변경
        LocalDateTime indicatedDate = request.getWorkOrderIndicatedDate();
        if (indicatedDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime seoulIndicatedDate = workOrderDomainService.convertIndicatedDate(indicatedDate);
        log.info("-------------- 작업지시일 시간대 변경 완료 : {} --------------", seoulIndicatedDate);

        //제품 납기일 시간대 변경
        LocalDateTime dueDate = request.getWorkOrderDueDate();
        if (dueDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime seoulDueDate =  workOrderDomainService.convertDueDate(dueDate);
        log.info("-------------- 작업지시일 시간대 변경 완료 : {} --------------", seoulIndicatedDate);

        // 납기일이 주문서 납기일보다 전이어야 함.
        if (dueDate.isAfter(salesOrder.getSalesOrderDueDate())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }

        // 작업지시서 생성
        WorkOrder workOrder = workOrderDomainService.createWorkOrder(
                request,
                salesOrderItem.getSalesOrder(),
                salesOrderItem,
                warehouse,
                user,
                workOrderName,
                workOrderIndicatedQuantity,
                seoulDueDate,
                seoulIndicatedDate
        );

        // 작업지시서 저장
        workOrderDomainService.saveWorkOrder(workOrder);
    }

    /* 작업지시서 수정 */
    @Transactional
    public void updateWorkOrder(Long workOrderSeq, UpdateWorkOrderRequest request) {
        log.info("-------------- 작업지시서 수정 서비스 진입 - {}번 수정, 수정요청 조건 - request: {}   --------------", workOrderSeq, request);
        log.info("workOrderIndicatedQuantity 값 확인: {}", request.getWorkOrderIndicatedQuantity());

        // 작업지시서 찾기
        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(workOrderSeq);
        log.info("작업지시서 조회 완료 - 작업지시서 번호: {}", workOrderSeq);

        // 작업지시서 상태에 따른 수정 로직
        switch (workOrder.getWorkOrderStatus()) {
            case BEFORE:
                // 결재 전 상태 - 모든 필드 수정 가능
                updateWorkOrderAllFields(workOrder, request);
                break;

            case ONGOING:
                // 진행 중 상태 - 제한된 필드만 수정 가능
                updateWorkOrderLimitedFields(workOrder, request);
                break;

            default:
                throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
        }

    }

    private void updateWorkOrderLimitedFields(WorkOrder workOrder, UpdateWorkOrderRequest request) {
        log.info("진행 중 상태 - 제한된 필드 수정 진행");

        // 제품 납기일 시간대 변경
        LocalDateTime dueDate = request.getWorkOrderDueDate();
        if (dueDate == null) {
            dueDate = workOrder.getWorkOrderDueDate(); // 기존 값 유지
            log.info("목표일이 입력되지 않아 기존 값 유지: {}", dueDate);
        }

        LocalDateTime seoulDueDate =  workOrderDomainService.convertDueDate(dueDate);
        log.info("-------------- 작업목표일 시간대 변경 완료 : {} --------------", seoulDueDate);

        SalesOrder salesOrder = workOrder.getSalesOrder();
        // 납기일이 주문서 납기일보다 전이어야 함.
        if (dueDate.isAfter(salesOrder.getSalesOrderDueDate())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }

        // 지시수량 변경
        Integer updatedQuantity = request.getWorkOrderIndicatedQuantity();
        if (updatedQuantity == null) {
            updatedQuantity = workOrder.getWorkOrderIndicatedQuantity(); // 기존 값 유지
            log.info("지시 수량이 입력되지 않아 기존 값 유지: {}", updatedQuantity);
        }
        // 지시수량 검증: 완료된 작업량보다 작으면 예외 발생
        if (updatedQuantity  < workOrder.getWorkOrderWorkQuantity()) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_INVALID_QUANTITY);
        }

        // 납기일과 수량, 비고만 수정
        workOrderDomainService.updateWorkOrder(
                workOrder,
                null, // SalesOrder 수정 불가
                null, // SalesOrderItem 수정 불가
                null, // Warehouse 수정 불가
                null, // 지시일 수정 불가
                seoulDueDate, // 납기일 수정
                updatedQuantity , // 지시수량 수정
                request.getWorkOrderNote() // 비고 수정
        );
    }

    private void updateWorkOrderAllFields(WorkOrder workOrder, UpdateWorkOrderRequest request){
        log.info("결재 전 상태 - 모든 필드 수정 진행");

        SalesOrder salesOrder = null;
        SalesOrderItem salesOrderItem = null;

        // 주문서 및 품목 확인
        if (NullCheck.nullOrZeroCheck(request.getSalesOrderSeq())) {
            salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
            // 주문서 상태 체크(결재 후인지)
            salesOrderDomainService.checkSalesOrderStatus(salesOrder.getSalesOrderStatus());
            log.info("주문서 상태 확인 완료 - 주문서 번호: {}, 상태: {}", request.getSalesOrderSeq(), salesOrder.getSalesOrderStatus());

            // 주문서 품목 확인(주문서 품목이 변경됐을 경우에만)
            if (NullCheck.nullOrZeroCheck(request.getSalesOrderItemSeq())) {
                salesOrderItem = salesOrderItemDomainService.findBySalesOrderItemSeq(request.getSalesOrderItemSeq())
                        .orElseThrow(() -> new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_FOUND));
                log.info("주문서 품목 확인 완료 - 품목 번호: {}", request.getSalesOrderItemSeq());

                // 품목 변경된 경우 중복 및 BOM 재고 검증
                if (!salesOrderItem.getItem().getItemSeq().equals(workOrder.getItem().getItemSeq())) {
                    workOrderDomainService.validateAndCheckForDuplicates(salesOrder, salesOrderItem, request.getWorkOrderIndicatedQuantity());
                }
            }

        }

        // 생산공장 확인 (생산공장이 변경됐을 경우에만)
        Warehouse warehouse = null;
        if (NullCheck.nullOrZeroCheck(request.getWarehouseSeq())) {
            warehouse = warehouseDomainService.findById(request.getWarehouseSeq());
            log.info("생산공장 확인 완료 - 생산공장 번호: {}", request.getWarehouseSeq());
        }

        // 작업지시일 시간대 변경
        LocalDateTime indicatedDate = request.getWorkOrderIndicatedDate();
        if (indicatedDate == null) {
            indicatedDate = workOrder.getWorkOrderIndicatedDate(); // 기존 값 유지
            log.info("지시일이 입력되지 않아 기존 값 유지: {}", indicatedDate);
        }
        LocalDateTime seoulIndicatedDate = workOrderDomainService.convertIndicatedDate(indicatedDate);
        log.info("-------------- 작업지시일 시간대 변경 완료 : {} --------------", seoulIndicatedDate);

        // 제품 납기일 시간대 변경
        LocalDateTime dueDate = request.getWorkOrderDueDate();
        if (dueDate == null) {
            dueDate = workOrder.getWorkOrderDueDate(); // 기존 값 유지
            log.info("목표일이 입력되지 않아 기존 값 유지: {}", dueDate);
        }
        // 납기일이 주문서 납기일보다 전이어야 함.
        if (dueDate.isAfter(workOrder.getSalesOrder().getSalesOrderDueDate())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }

        LocalDateTime seoulDueDate =  workOrderDomainService.convertDueDate(dueDate);
        log.info("-------------- 작업목표일 시간대 변경 완료 : {} --------------", seoulDueDate);

        // 지시수량 변경
        Integer updatedQuantity = request.getWorkOrderIndicatedQuantity();
        if (updatedQuantity == null) {
            updatedQuantity = workOrder.getWorkOrderIndicatedQuantity(); // 기존 값 유지
            log.info("지시 수량이 입력되지 않아 기존 값 유지: {}", updatedQuantity);
        }

        // 작업지시서 수정
        workOrderDomainService.updateWorkOrder(
                workOrder,
                salesOrder,
                salesOrderItem,
                warehouse,
                seoulIndicatedDate,
                seoulDueDate,
                updatedQuantity,
                request.getWorkOrderNote()
        );
    }

    /* 작업지시서 삭제 */
    @Transactional
    public void deleteWorkOrder(Long workOrderSeq) {
        log.info("-------------- 작업지시서 삭제 서비스 진입 - {}번 삭제 --------------", workOrderSeq);

        // 작업지시서 찾기
        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(workOrderSeq);

        // 삭제가능한 상태인지 체크
        workOrderDomainService.checkWorkOrderStatusDeletePossible(workOrder.getWorkOrderStatus());

        // 삭제로 상태 변경
        workOrderDomainService.deleteWorkOrder(workOrder);
    }

    /* 상태 변경 */
    @Transactional
    public void updateWorkOrderStatus(Long workOrderSeq, WorkOrderStatus newStatus) {
        log.info("-------------- 작업지시서 상태 변경 서비스 진입 - {}번 변경 --------------", workOrderSeq);
        // 작업지시서 찾기
        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(workOrderSeq);
        WorkOrderStatus currentStatus  = workOrder.getWorkOrderStatus();

        switch (newStatus) {
            case AFTER:
                log.info("-------------- 작업지시서 결재상태 변경 서비스 진입 - {}번 결재 전>후 변경, 현재상태 : {} --------------", workOrderSeq, currentStatus);
                if (!currentStatus.equals(WorkOrderStatus.BEFORE)) {
                    throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
                }
                break;

            case STOP:
                log.info("-------------- 작업지시서 진행상태 변경 서비스 진입 - {}번 중단으로 변경, 현재상태 : {}  --------------", workOrderSeq, currentStatus);
                if (!currentStatus.equals(WorkOrderStatus.ONGOING)) {
                    throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
                }
                break;

            case ONGOING:
                log.info("-------------- 작업지시서 진행상태 변경 서비스 진입 - {}번 진행중으로 변경, 현재상태 : {}  --------------", workOrderSeq, currentStatus);
                if (!(currentStatus.equals(WorkOrderStatus.STOP) || currentStatus.equals(WorkOrderStatus.AFTER))) {
                    throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
                }
                break;

            case COMPLETE:
                log.info("-------------- 작업지시서 진행상태 변경 서비스 진입 - {}번 완료로 변경, 현재상태 : {}  --------------", workOrderSeq, currentStatus);
                if (!currentStatus.equals(WorkOrderStatus.ONGOING)) {
                    throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
                }

                Integer workOrderIndicatedQuantity = workOrder.getWorkOrderIndicatedQuantity();
                workOrderDomainService.updateWorkOrderWorkAutoComplete(workOrder, workOrderIndicatedQuantity);

                break;

            default:
                throw new CustomException(ErrorCodeType.WORK_ORDER_STATE_BAD_REQUEST);
        }

        // 상태 변경
        workOrderDomainService.updateWorkOrderStatus(workOrder, newStatus);
        log.info("-------------- 작업지시서 상태 변경 완료 - {}번, 새로운 상태: {} --------------", workOrderSeq, newStatus);
    }

}
