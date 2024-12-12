package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.repository.BomItemRepository;
import error.pirate.backend.item.command.domain.service.BomItemDomainService;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderItemRepository;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderItemDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import error.pirate.backend.user.command.domain.service.UserDomainService;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.warehouse.command.domain.service.WarehouseDomainService;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import error.pirate.backend.workOrder.command.domain.service.WorkOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Transactional
    public List<WorkOrder> createWorkOrder(CreateWorkOrderRequest request) {

        // 1. 주문서 상태 확인
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
        if (salesOrder.getSalesOrderStatus() != SalesOrderStatus.AFTER) {
            throw new CustomException(ErrorCodeType.SALES_ORDER_STATE_BAD_REQUEST);
        }

        // 2. 생산공장 확인
        Warehouse warehouse = warehouseDomainService.findById(request.getWarehouseSeq());

        // 3. 주문서 품목 가져오기
        List<SalesOrderItem> salesOrderItems = salesOrderItemDomainService.findAllBySalesOrderSeq(request.getSalesOrderSeq());

        // 4. 사용자 설정
        User user = userDomainService.findById(request.getUserSeq());

        // 5-1. 작업지시서 이름 설정을 위한 카운트
        long count = workOrderDomainService.countTodayWorkOrders();
        List<WorkOrder> workOrders = new ArrayList<>();

        for (SalesOrderItem salesOrderItem : salesOrderItems) {
            log.info("Processing SalesOrderItemSeq: {}", salesOrderItem.getSalesOrderItemSeq());
            log.debug("Checking existence for SalesOrderItemSeq: {}", salesOrderItem.getSalesOrderItemSeq());
            // 5-1. 중복 체크
            if (workOrderDomainService.existsBySalesOrderItem(salesOrderItem.getSalesOrderItemSeq())) {
                log.warn("WorkOrder already exists for SalesOrderItem: {}", salesOrderItem.getSalesOrderItemSeq());
                continue;
            }

            // 5-2. 작업지시서 이름 생성
            String workOrderName = workOrderDomainService.generateWorkOrderName(count);

            // 6. BOM 품목 및 재고 검증
            List<BomItem> bomItems = bomItemDomainService.findAllByParentItem(salesOrderItem.getItem());
            itemInventoryDomainService.checkInventoryForBomItems(bomItems, salesOrderItem.getSalesOrderItemQuantity());

            // 7. 작업지시서 관련 시간대를 서울 시간으로 변경
            // 작업지시일
            LocalDateTime indicatedDate = request.getWorkOrderIndicatedDate();
            if (indicatedDate == null) {
                throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
            }
            LocalDateTime seoulIndicatedDate = workOrderDomainService.convertToSeoulTime(indicatedDate);
            log.info("Original Indicated Date: {}, Converted to Seoul Time: {}", indicatedDate, seoulIndicatedDate);


            // 납기일
            LocalDateTime dueDate = request.getWorkOrderDueDate();
            if (dueDate == null) {
                throw new CustomException(ErrorCodeType.WORK_ORDER_REQUIRED_INFORMATION);
            }

            LocalDateTime seoulDueDate = workOrderDomainService.convertToSeoulTime(dueDate);
            log.info("Original Due Date: {}, Converted to Seoul Time: {}", dueDate, seoulDueDate);


            // 8. 작업지시서 생성
            WorkOrder workOrder = workOrderDomainService.createWorkOrder(request, salesOrder, salesOrderItem, warehouse, user, workOrderName, seoulDueDate, seoulIndicatedDate);

            // 9. 작업지시서 저장
            log.info("Saving WorkOrder: IndicatedDate={}, DueDate={}",
                    workOrder.getWorkOrderIndicatedDate(),
                    workOrder.getWorkOrderDueDate());

            workOrderDomainService.saveWorkOrder(workOrder);
            workOrders.add(workOrder);
        }
        return workOrders;
    }

}
