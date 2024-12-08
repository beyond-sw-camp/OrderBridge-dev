package error.pirate.backend.productionReceiving.command.application.service;

import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductionReceivingService {

    private final ProductionReceivingRepository productionReceivingRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final WorkOrderRepository workOrderRepository;

    @Transactional
    public void createProductionReceiving(ProductionReceivingCreateRequest request) {

        Warehouse productionWarehouse = warehouseRepository.findById(request.getProductionWarehouseSeq()).orElseThrow();
        Warehouse storeWarehouse = warehouseRepository.findById(request.getStoreWarehouseSeq()).orElseThrow();
        User user = userRepository.findById(request.getUserSeq()).orElseThrow();
        WorkOrder workOrder = workOrderRepository.findById(request.getWorkOrderSeq()).orElseThrow();

        ProductionReceiving productionReceiving = ProductionReceiving.createProductionReceiving(productionWarehouse, storeWarehouse, user, workOrder, request);

        productionReceivingRepository.save(productionReceiving);
    }
}
