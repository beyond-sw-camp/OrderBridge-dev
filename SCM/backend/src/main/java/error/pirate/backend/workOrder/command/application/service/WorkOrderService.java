package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.service.BomItemDomainService;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderService {

    private final WorkOrderDomainService workOrderDomainService;
    private final SalesOrderDomainService salesOrderDomainService;
    private final ItemInventoryDomainService itemInventoryDomainService;
    private final BomItemDomainService bomItemDomainService;
    private final UserDomainService userDomainService;
    private final WarehouseDomainService warehouseDomainService;

    @Transactional
    public void createWorkOrder(CreateWorkOrderRequest request) {

        // 1. 주문서 상태 확인
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
        if (salesOrder.getSalesOrderStatus() != SalesOrderStatus.AFTER) {
            throw new CustomException(ErrorCodeType.SALES_ORDER_STATE_BAD_REQUEST);
        }

        // 2. 생산공장 확인
        Warehouse warehouse = warehouseDomainService.findById(request.getWarehouseSeq());

        // 3. BOM 품목 및 재고 검증
        List<BomItem> bomItems = bomItemDomainService.calculateBomItems(request.getSalesOrderSeq());
        itemInventoryDomainService.checkInventoryForBomItems(bomItems, request.getWorkOrderIndicatedQuantity());

        // 4. 사용자 설정
        User user = userDomainService.findById(request.getUserSeq());

        // 5. 작업지시서 이름 생성
        long count = workOrderDomainService.countTodayWorkOrders();
        String workOrderName = workOrderDomainService.generateWorkOrderName(count);

        // 6. 작업지시서 생성
        WorkOrder workOrder = workOrderDomainService.createWorkOrder(request, salesOrder, warehouse, user, workOrderName);

        // 7. 작업지시서 저장
        workOrderDomainService.saveWorkOrder(workOrder);

        // 8. 작업지시서 관련 사용자와 시간 처리 (서울 시간 기준)
        LocalDateTime seoulTime = workOrderDomainService.getSeoulCurrentTime();
        workOrder.setSeoulIndicatedTime(seoulTime);
    }

}
