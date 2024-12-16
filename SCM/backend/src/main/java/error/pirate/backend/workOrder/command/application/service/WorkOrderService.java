package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.service.BomItemDomainService;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseStatus;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderItemDomainService;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
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
import java.time.LocalTime;
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
        if (salesOrder.getSalesOrderStatus() != SalesOrderStatus.AFTER) {
            throw new CustomException(ErrorCodeType.SALES_ORDER_STATE_BAD_REQUEST);
        }

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

        // 8. 시간 처리 (작업지시일 및 납기일)
        // 작업시일
        LocalDate indicatedDate = request.getWorkOrderIndicatedDate();
        if (indicatedDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime regDate = LocalDateTime.now();
        LocalDateTime seoulIndicatedDate;
        // 작업지시일과 등록일의 날짜가 같은 경우 처리
        if (indicatedDate.isEqual(regDate.toLocalDate())) {
            seoulIndicatedDate = regDate.plusHours(1); // 등록일보다 1시간 이후로 설정
            log.info("-------------- 작업지시일 = 등록일, 등록일 + 1시간 : {} --------------", seoulIndicatedDate);
        } else {
            seoulIndicatedDate = workOrderDomainService.setIndicatedDate(indicatedDate.atTime(LocalTime.of(9, 0)));
            log.info("-------------- 바뀐 작업지시일 : {} --------------", seoulIndicatedDate);
        }

        // 제품 납기일
        LocalDate dueDate = request.getWorkOrderDueDate();
        if (dueDate == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
        }
        LocalDateTime seoulDueDate = workOrderDomainService.setDueDate(dueDate.atTime(LocalTime.of(23, 59, 59)));

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

    @Transactional
    public void deleteWorkOrder(Long workOrderSeq) {
        log.info("-------------- 작업지시서 삭제 서비스 진입 - {}번  --------------", workOrderSeq);

        // 작업지시서 찾기
        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(workOrderSeq);

        // 결재상태 체크
        workOrderDomainService.checkWorkOrderStatus(workOrder.getWorkOrderStatus());

        // 삭제로 상태 변경
        workOrderDomainService.deleteWorkOrder(workOrder);
    }

}
