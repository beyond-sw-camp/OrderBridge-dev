package error.pirate.backend.productionReceiving.command.application.service;

import error.pirate.backend.common.NullCheck;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingItemDTO;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingUpdateRequest;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingItem;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingItemRepository;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingRepository;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionReceivingService {

    private final ProductionReceivingRepository productionReceivingRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ItemRepository itemRepository;
    private final ProductionReceivingItemRepository productionReceivingItemRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Transactional
    public void createProductionReceiving(ProductionReceivingCreateRequest request) {

        Warehouse productionWarehouse = warehouseRepository.findById(request.getProductionWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));
        Warehouse storeWarehouse = warehouseRepository.findById(request.getStoreWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));
        User user = userRepository.findById(request.getUserSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));
        WorkOrder workOrder = workOrderRepository.findById(request.getWorkOrderSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WORK_ORDER_NOT_FOUND));
        if(!WorkOrderStatus.COMPLETION.equals(workOrder.getWorkOrderStatus())) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_STATUS_ERROR);
        }
        ProductionReceiving productionReceiving = ProductionReceiving.createProductionReceiving(productionWarehouse, storeWarehouse, user, workOrder, request);

        productionReceivingRepository.save(productionReceiving);

        for(ProductionReceivingItemDTO dto : request.getProductionReceivingItemList()) {
            Item item = itemRepository.findById(dto.getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));

            ProductionReceivingItem productionReceivingItem = ProductionReceivingItem.createProductionReceivingItem(item, productionReceiving, dto);
            productionReceivingItemRepository.save(productionReceivingItem);
        }
    }

    @Transactional
    public void updateProductionReceiving(Long productionReceivingSeq, ProductionReceivingUpdateRequest request) {
        ProductionReceiving productionReceiving = productionReceivingRepository.findById(productionReceivingSeq).orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_NOT_FOUND));
        if(!ProductionReceivingStatus.BEFORE.equals(productionReceiving.getProductionReceivingStatus())) {
            throw new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_UPDATE_ERROR);
        }
        Warehouse productionWarehouse = null;
        Warehouse storeWarehouse = null;
        if(NullCheck.nullOrZeroCheck(request.getProductionWarehouseSeq())) {
            productionWarehouse = warehouseRepository.findById(request.getProductionWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));
        }

        if(NullCheck.nullOrZeroCheck(request.getStoreWarehouseSeq())) {
            storeWarehouse = warehouseRepository.findById(request.getStoreWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));
        }

        productionReceiving.updateProductionReceiving(productionWarehouse, storeWarehouse, request);

        /* 생산 입고 품목 수정 시 모두 삭제 처리 후 재등록 */
        List<ProductionReceivingItem> productionReceivingItems = productionReceivingItemRepository.findAllByProductionReceiving(productionReceiving);
        productionReceivingItemRepository.deleteAllInBatch(productionReceivingItems);

        if(NullCheck.nullCheck(request.getProductionReceivingItemList())) {
            for(ProductionReceivingItemDTO dto : request.getProductionReceivingItemList()) {
                Item item = itemRepository.findById(dto.getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));

                ProductionReceivingItem productionReceivingItem = ProductionReceivingItem.createProductionReceivingItem(item, productionReceiving, dto);
                productionReceivingItemRepository.save(productionReceivingItem);
            }
        }
    }

    @Transactional
    public void deleteProductionReceiving(Long productionReceivingSeq) {
        ProductionReceiving productionReceiving = productionReceivingRepository.findById(productionReceivingSeq).orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_NOT_FOUND));
        if(!ProductionReceivingStatus.BEFORE.equals(productionReceiving.getProductionReceivingStatus())) {
            throw new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_UPDATE_ERROR);
        }

        productionReceivingRepository.delete(productionReceiving);
    }

    @Transactional
    public void updateProductionReceivingApproval(Long productionReceivingSeq) {
        ProductionReceiving productionReceiving = productionReceivingRepository.findById(productionReceivingSeq).orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_NOT_FOUND));
        if(!ProductionReceivingStatus.BEFORE.equals(productionReceiving.getProductionReceivingStatus())) {
            throw new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_UPDATE_ERROR);
        }

        productionReceiving.updateProductionReceivingApproval();
    }

    @Transactional
    public void updateProductionReceivingComplete(Long productionReceivingSeq) {
        ProductionReceiving productionReceiving = productionReceivingRepository.findById(productionReceivingSeq).orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_NOT_FOUND));
        if(!ProductionReceivingStatus.AFTER.equals(productionReceiving.getProductionReceivingStatus())) {
            throw new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_UPDATE_COMPLETE_ERROR);
        }

        SalesOrder salesOrder = salesOrderRepository.findByProductionReceivingSeq(productionReceivingSeq);
    }
}
