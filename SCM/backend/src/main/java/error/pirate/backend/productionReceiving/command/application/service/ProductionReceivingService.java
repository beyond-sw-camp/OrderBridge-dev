package error.pirate.backend.productionReceiving.command.application.service;

import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.common.NullCheck;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import error.pirate.backend.item.command.domain.repository.ItemInventoryRepository;
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
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionReceivingService {

    private final ProductionReceivingRepository productionReceivingRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ItemRepository itemRepository;
    private final ProductionReceivingItemRepository productionReceivingItemRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final NameGenerator nameGenerator;

    @Transactional
    public void createProductionReceiving(ProductionReceivingCreateRequest request) {

        request.setUserSeq(1L);
        User user = userRepository.findById(request.getUserSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));

        request.setProductionReceivingName(nameGenerator.nameGenerator(ProductionReceiving.class));
        ProductionReceiving productionReceiving = ProductionReceiving.createProductionReceiving(user, request);

        productionReceivingRepository.save(productionReceiving);

        for(Long workOrderSeq : request.getWorkOrderSeqList()) {
            WorkOrder workOrder = workOrderRepository.findById(workOrderSeq).orElseThrow(() -> new CustomException(ErrorCodeType.WORK_ORDER_NOT_FOUND));

            if(!WorkOrderStatus.ONGOING.equals(workOrder.getWorkOrderStatus())) { // 생산 불출 됐다는 의미
                throw new CustomException(ErrorCodeType.WORK_ORDER_STATUS_ERROR);
            }
            workOrder.createProductionReceiving(productionReceiving);
        }

        if(NullCheck.nullCheck(request.getProductionReceivingItemList())) {
            for(ProductionReceivingItemDTO dto : request.getProductionReceivingItemList()) {
                Item item = itemRepository.findById(dto.getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
                Warehouse warehouse = warehouseRepository.findById(item.getWarehouse().getWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));

                ProductionReceivingItem productionReceivingItem = ProductionReceivingItem.createProductionReceivingItem(item, productionReceiving, warehouse, dto);
                productionReceivingItemRepository.save(productionReceivingItem);
            }
        }
    }

    @Transactional
    public void updateProductionReceiving(Long productionReceivingSeq, ProductionReceivingUpdateRequest request) {
        ProductionReceiving productionReceiving = productionReceivingRepository.findById(productionReceivingSeq).orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_NOT_FOUND));
        if(!ProductionReceivingStatus.BEFORE.equals(productionReceiving.getProductionReceivingStatus())) {
            throw new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_UPDATE_ERROR);
        }

        productionReceiving.updateProductionReceiving(request);

        List<WorkOrder> workOrders = workOrderRepository.findByProductionReceiving(productionReceiving);

        // 기존의 작업지시서 상태 값을 원래대로 되돌린다.
        for(WorkOrder workOrder : workOrders) {
            if(!WorkOrderStatus.COMPLETE.equals(workOrder.getWorkOrderStatus())) {
                workOrder.deleteProductionReceiving();
            }
        }

        for(Long workOrderSeq : request.getWorkOrderSeqList()) {
            WorkOrder workOrder = workOrderRepository.findById(workOrderSeq).orElseThrow(() -> new CustomException(ErrorCodeType.WORK_ORDER_NOT_FOUND));

            if(!WorkOrderStatus.ONGOING.equals(workOrder.getWorkOrderStatus())) {
                throw new CustomException(ErrorCodeType.WORK_ORDER_STATUS_ERROR);
            }
            workOrder.createProductionReceiving(productionReceiving);
        }

        /* 생산 입고 품목 수정 시 모두 삭제 처리 후 재등록 */
        List<ProductionReceivingItem> productionReceivingItems = productionReceivingItemRepository.findAllByProductionReceiving(productionReceiving);
        productionReceivingItemRepository.deleteAllInBatch(productionReceivingItems);

        if(NullCheck.nullCheck(request.getProductionReceivingItemList())) {
            for(ProductionReceivingItemDTO dto : request.getProductionReceivingItemList()) {
                Item item = itemRepository.findById(dto.getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
                Warehouse warehouse = warehouseRepository.findById(item.getWarehouse().getWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));

                ProductionReceivingItem productionReceivingItem = ProductionReceivingItem.createProductionReceivingItem(item, productionReceiving, warehouse,dto);
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

        // 생산입고 삭제 시 작업지시서 생산입고 값 null로 변경 후 상태 값 변경
        List<WorkOrder> workOrders = workOrderRepository.findByProductionReceiving(productionReceiving);
        for(WorkOrder workOrder : workOrders) {
            workOrder.deleteProductionReceiving();
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

    // 생산입고 완료
    @Transactional
    public void updateProductionReceivingComplete(Long productionReceivingSeq) {
        ProductionReceiving productionReceiving = productionReceivingRepository.findById(productionReceivingSeq).orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_NOT_FOUND));
        if(!ProductionReceivingStatus.AFTER.equals(productionReceiving.getProductionReceivingStatus())) {
            throw new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_UPDATE_COMPLETE_ERROR);
        }

        productionReceiving.updateProductionReceivingComplete(); // 생산완료 상태 변경

        SalesOrder salesOrder = salesOrderRepository.findByProductionReceivingSeq(productionReceivingSeq);

        salesOrder.updateSalesOrderStatus(SalesOrderStatus.PRODUCTION_COMPLETE); // 주문서의 상태를 생산완료로 변경

        /*
        * 품목 재고에 생산입고 수량만큼 추가
        * */
        List<ProductionReceivingItem> productionReceivingItems = productionReceivingItemRepository.findAllByProductionReceiving(productionReceiving);
        for(ProductionReceivingItem productionReceivingItem : productionReceivingItems) {
            Item item = itemRepository.findById(productionReceivingItem.getItem().getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
            ItemInventory itemInventory = ItemInventory.createItemInventory(
                    item,
                    productionReceivingItem.getProductionReceivingItemQuantity(),
                    productionReceivingItem.getProductionReceivingItemQuantity());
            itemInventoryRepository.save(itemInventory);
        }
    }
}
