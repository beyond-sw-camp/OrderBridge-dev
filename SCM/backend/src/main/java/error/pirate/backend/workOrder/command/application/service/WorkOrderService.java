package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.common.NullCheck;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.service.BomItemDomainService;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderItemDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.service.UserDomainService;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.service.WarehouseDomainService;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.application.dto.UpdateWorkOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.service.WorkOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderService {

    private final WorkOrderDomainService workOrderDomainService;
    private final SalesOrderDomainService salesOrderDomainService;
    private final SalesOrderItemDomainService salesOrderItemDomainService;
    private final ItemInventoryDomainService itemInventoryDomainService;
    private final BomItemDomainService bomItemDomainService;
    private final UserDomainService userDomainService;
    private final WarehouseDomainService warehouseDomainService;
    private final NameGenerator nameGenerator;

    /* 작업지시서 등록 */
    @Transactional
    public void createWorkOrderForItem(CreateWorkOrderRequest request) {
        log.info("-------------- 작업지시서 등록 서비스 진입 :등록요청 조건 - request: {} --------------", request);

        // 1. 주문서 상태 확인
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
        // 1-1. 주문서의 결재상태 확인
        salesOrderDomainService.checkSalesOrderStatus(salesOrder.getSalesOrderStatus());

        // 2. 주문서 품목 가져오기
        SalesOrderItem salesOrderItem = salesOrderItemDomainService.findBySalesOrderItemSeq(request.getSalesOrderItemSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_FOUND));

        Long itemSeq = salesOrderItem.getItem().getItemSeq();
        log.info("-------------- 찾은 ItemSeq: {}--------------", itemSeq);

        // 3. 생산공장 확인
        Warehouse warehouse = warehouseDomainService.findById(request.getWarehouseSeq());

        // 4. 사용자 설정
        User user = userDomainService.findById(request.getUserSeq());

        // 5. 작업지시서명 설정
        request.setWorkOrderName(nameGenerator.nameGenerator(WorkOrder.class));

        // 6.  주문서번호와 품목번호로(같은 주문서에서는 같은 품목 주문이 여러 개 들어오지 않는다는 가정) 중복체크
        if (workOrderDomainService.existsBySalesOrderAndItem(salesOrder.getSalesOrderSeq(), itemSeq)) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_DUPLICATE);
        }

        // 7. BOM 품목 및 재고 검증
        List<BomItem> bomItems = bomItemDomainService.findAllByParentItem(salesOrderItem.getItem());
        itemInventoryDomainService.checkInventoryForBomItems(bomItems, salesOrderItem.getSalesOrderItemQuantity());

        // 8-1. 작업지시일 시간대 변경
        LocalDate indicatedDate = request.getWorkOrderIndicatedDate();
        if (indicatedDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime seoulIndicatedDate = workOrderDomainService.convertIndicatedDate(indicatedDate);
        log.info("-------------- 작업지시일 시간대 변경 완료 : {} --------------", seoulIndicatedDate);

        // 8-2. 제품 납기일 시간대 변경
        LocalDate dueDate = request.getWorkOrderDueDate();
        if (dueDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime seoulDueDate =  workOrderDomainService.convertDueDate(dueDate);
        log.info("-------------- 작업지시일 시간대 변경 완료 : {} --------------", seoulIndicatedDate);

        // 9. 작업지시서 생성
        WorkOrder workOrder = workOrderDomainService.createWorkOrder(
                request,
                salesOrderItem.getSalesOrder(),
                salesOrderItem,
                warehouse,
                user,
                seoulDueDate,
                seoulIndicatedDate
        );

        // 10. 작업지시서 저장
        workOrderDomainService.saveWorkOrder(workOrder);
    }

    /* 작업지시서 수정 */
    @Transactional
    public void updateWorkOrder(Long workOrderSeq, UpdateWorkOrderRequest request) {
        log.info("-------------- 작업지시서 수정 서비스 진입 - {}번 수정, 수정요청 조건 - request: {}   --------------", workOrderSeq, request);

        // 작업지시서 찾기
        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(workOrderSeq);
        log.info("작업지시서 조회 완료 - 작업지시서 번호: {}", workOrderSeq);

        // 작업지시서 결재상태 체크
//        workOrderDomainService.checkWorkOrderStatus(workOrder.getWorkOrderStatus());
//        log.info("작업지시서 상태 확인 완료 - 상태: {}", workOrder.getWorkOrderStatus());

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

        // 지시수량 검증: 완료된 작업량보다 작으면 예외 발생
        if (request.getWorkOrderIndicatedQuantity() < workOrder.getWorkOrderWorkQuantity()) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_INVALID_QUANTITY);
        }

        // 납기일과 비고만 수정
        workOrderDomainService.updateWorkOrder(
                workOrder,
                null, // SalesOrder 수정 불가
                null, // SalesOrderItem 수정 불가
                null, // Warehouse 수정 불가
                null, // 지시일 수정 불가
                workOrderDomainService.convertDueDate(request.getWorkOrderDueDate()), // 납기일 수정
                request.getWorkOrderIndicatedQuantity(), // 지시수량 수정
                request.getWorkOrderNote() // 비고 수정
        );
    }

    private void updateWorkOrderAllFields(WorkOrder workOrder, UpdateWorkOrderRequest request){
        log.info("결재 전 상태 - 모든 필드 수정 진행");

        SalesOrder salesOrder = null;
        SalesOrderItem salesOrderItem = null;

        // 주문서 및 품목 확인
        if (NullCheck.nullOrZeroCheck(request.getSalesOrderSeq())) {
            SalesOrder newSalesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
            // 주문서 상태 체크(결재 후인지)
            salesOrderDomainService.checkSalesOrderStatus(newSalesOrder.getSalesOrderStatus());
            log.info("주문서 상태 확인 완료 - 주문서 번호: {}, 상태: {}", request.getSalesOrderSeq(), newSalesOrder.getSalesOrderStatus());

            // 주문서 품목 확인(주문서 품목이 변경됐을 경우에만)
            if (NullCheck.nullOrZeroCheck(request.getSalesOrderItemSeq())) {
                SalesOrderItem newSalesOrderItem = salesOrderItemDomainService.findBySalesOrderItemSeq(request.getSalesOrderItemSeq())
                        .orElseThrow(() -> new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_FOUND));
                log.info("주문서 품목 확인 완료 - 품목 번호: {}", request.getSalesOrderItemSeq());

                // 품목이 기존과 다를 경우에만 중복 검사 실행
                if (!newSalesOrderItem.getItem().getItemSeq().equals(workOrder.getItem().getItemSeq())) {
                    if (workOrderDomainService.existsBySalesOrderAndItem(newSalesOrder.getSalesOrderSeq(), newSalesOrderItem.getItem().getItemSeq())) {
                        throw new CustomException(ErrorCodeType.WORK_ORDER_DUPLICATE);
                    }
                    log.info("중복 작업지시서 확인 완료 - 주문서 번호: {}, 품목 번호: {}", newSalesOrder.getSalesOrderSeq(), newSalesOrderItem.getItem().getItemSeq());
                }

                // 작업지시서 중복여부 확인(주문서번호와 품목번호로 중복체크)
                if (newSalesOrderItem != null) {
                    if (workOrderDomainService.existsBySalesOrderAndItem(newSalesOrder.getSalesOrderSeq(), newSalesOrderItem.getItem().getItemSeq())) {
                        throw new CustomException(ErrorCodeType.WORK_ORDER_DUPLICATE);
                    }
                    log.info("중복 작업지시서 확인 완료 - 주문서 번호: {}, 품목 번호: {}", newSalesOrder.getSalesOrderSeq(), newSalesOrderItem.getItem().getItemSeq());
                }

                // 변경된 경우 BOM 품목 및 재고 검증
                List<BomItem> bomItems = bomItemDomainService.findAllByParentItem(newSalesOrderItem.getItem());
                itemInventoryDomainService.checkInventoryForBomItems(bomItems, request.getWorkOrderIndicatedQuantity());

            }

        }

        // 생산공장 확인 (생산공장이 변경됐을 경우에만)
        Warehouse warehouse = null;
        if (NullCheck.nullOrZeroCheck(request.getWarehouseSeq())) {
            warehouse = warehouseDomainService.findById(request.getWarehouseSeq());
            log.info("생산공장 확인 완료 - 생산공장 번호: {}", request.getWarehouseSeq());
        }

        // 납기일 유효성 검사
        if (request.getWorkOrderDueDate().isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }
        log.info("납기일 유효성 검증 완료 - 납기일: {}", request.getWorkOrderDueDate());

        // 작업지시일 시간대 변경
        LocalDate indicatedDate = request.getWorkOrderIndicatedDate();
        if (indicatedDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime seoulIndicatedDate = workOrderDomainService.convertIndicatedDate(indicatedDate);
        log.info("-------------- 작업지시일 시간대 변경 완료 : {} --------------", seoulIndicatedDate);

        // 제품 납기일 시간대 변경
        LocalDate dueDate = request.getWorkOrderDueDate();
        if (dueDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime seoulDueDate =  workOrderDomainService.convertDueDate(dueDate);
        log.info("-------------- 작업지시일 시간대 변경 완료 : {} --------------", seoulIndicatedDate);

        // 5. 작업지시서 수정
        workOrderDomainService.updateWorkOrder(
                workOrder,
                salesOrder,
                salesOrderItem,
                warehouse,
                seoulIndicatedDate,
                seoulDueDate,
                request.getWorkOrderIndicatedQuantity(),
                request.getWorkOrderNote()
        );
    }

    @Transactional
    public void deleteWorkOrder(Long workOrderSeq) {
        log.info("-------------- 작업지시서 삭제 서비스 진입 - {}번 삭제 --------------", workOrderSeq);

        // 작업지시서 찾기
        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(workOrderSeq);

        // 결재상태 체크
        workOrderDomainService.checkWorkOrderStatus(workOrder.getWorkOrderStatus());

        // 삭제로 상태 변경
        workOrderDomainService.deleteWorkOrder(workOrder);
    }

}
