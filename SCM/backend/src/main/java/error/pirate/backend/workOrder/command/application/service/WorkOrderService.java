package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderDomainService salesOrderDomainService;

    @Transactional
    public void createWorkOrder(CreateWorkOrderRequest request) {
        Warehouse productionWarehouse = warehouseRepository.findById(request.getProductionWarehouseSeq()).orElseThrow();

        SalesOrder salesOrder = salesOrderDomainService.findBySalesOrderName(request.getSalesOrderName());

        // 출하지시서 유저는 주문서 작성 유저
        User user = salesOrder.getUser();
        WorkOrder workOrder = WorkOrder.createWorkOrder(productionWarehouse, user, salesOrder, request);
        workOrderRepository.save(workOrder);

    }
}
