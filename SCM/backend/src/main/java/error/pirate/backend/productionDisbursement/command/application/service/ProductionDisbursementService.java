package error.pirate.backend.productionDisbursement.command.application.service;

import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.item.command.domain.service.BomItemDomainService;
import error.pirate.backend.item.command.domain.service.ItemDomainService;
import error.pirate.backend.productionDisbursement.command.application.dto.CreateAndUpdateProductionDisbursementRequest;
import error.pirate.backend.productionDisbursement.command.application.dto.ProductionDisbursementItemRequest;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementItem;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.repository.ProductionDisbursementItemRepository;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.service.ProductionDisbursementDomainService;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import error.pirate.backend.workOrder.command.domain.service.WorkOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionDisbursementService {

    private final ProductionDisbursementDomainService productionDisbursementDomainService;
    private final WorkOrderDomainService workOrderDomainService;
    private final NameGenerator nameGenerator;
    private final ItemDomainService itemDomainService;
    private final BomItemDomainService bomItemDomainService;
    private final ProductionDisbursementItemRepository productionDisbursementItemRepository;
    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;
    private final SalesOrderDomainService salesOrderDomainService;

    // 등록
    @Transactional
    public void createProductionDisbursement(CreateAndUpdateProductionDisbursementRequest request) {
        log.info("-------------- 생산불출 등록 서비스 진입 :등록요청 조건 - request: {} --------------", request);

        // 작업지시서 상태 확인
        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(request.getWorkOrderSeq());

        // 작업지시서 결재상태 확인
        workOrderDomainService.checkWorkOrderApprovalStatus(workOrder.getWorkOrderStatus());

        // 이미 선택된 작업지시서인지 확인(중복체크)
        if (productionDisbursementDomainService.existsByWorkOrderSeq(request.getWorkOrderSeq())) {
            throw new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_DUPLICATE);
        }

        // 사용자 설정 - 작업지시서 작성 유저
        User user = workOrder.getUser();

        // 생산불출명 설정
        String productionDisbursementName = nameGenerator.nameGenerator(ProductionDisbursement.class);
        log.info("-------------- 생성된 productionDisbursementName: {} -------------- ", productionDisbursementName);

        // 작업 품목 확인 및 창고 정보 확인
        Item orderedItem = itemDomainService.findById(workOrder.getItem().getItemSeq());
        Warehouse warehouse = orderedItem.getWarehouse(); // 품목에 연결된 창고 정보 가져오기
        if (warehouse == null) {
            throw new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND);
        }

        // 지시수량 확인
        int indicatedQuantity = workOrder.getWorkOrderIndicatedQuantity();
        // BOM 재고 검증 및 차감
        bomItemDomainService.validateAndDeductStock(orderedItem, indicatedQuantity);

        // 생산불출일 유효값 검증
        LocalDateTime productionDisbursementDepartureDate = request.getProductionDisbursementDepartureDate();
        if (productionDisbursementDepartureDate == null) {
            throw new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_REQUIRED_INFORMATION);
        }

        // 불출일이 작업지시서 납기일보다 전이어야 함.
        if (productionDisbursementDepartureDate.isAfter(workOrder.getWorkOrderDueDate())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }

        // 불출일이 작업지시서 지시일 이후 이어야 함.
        if (productionDisbursementDepartureDate.isBefore(workOrder.getWorkOrderIndicatedDate())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }

        // 생산불출일 시간대 변경
        LocalDateTime seoulDepartureDate = productionDisbursementDomainService.convertDate(productionDisbursementDepartureDate);
        request.setProductionDisbursementDepartureDate(seoulDepartureDate);
        log.info("-------------- 불출일 시간대 변경 완료 : {} --------------", seoulDepartureDate);

        // 생산불출 생성
        ProductionDisbursement newProductionDisbursement =
                productionDisbursementDomainService.createProductionDisbursement(request, workOrder, user, productionDisbursementName);

        // 생산불출 품목 생성 및 총 불출량 계산
        int totalDisbursementQuantity = 0;
        List<BomItem> bomItems = bomItemDomainService.findAllByParentItem(orderedItem); // BOM 항목 조회
        Map<Long, String> itemNotes = request.getItemRequests().stream()
                .filter(dto -> dto.getItemSeq() != null && dto.getNote() != null)
                .collect(Collectors.toMap(
                        ProductionDisbursementItemRequest::getItemSeq,
                        ProductionDisbursementItemRequest::getNote
                ));


        for (BomItem bomItem : bomItems) {
            Item subItem = itemDomainService.findById(bomItem.getChildItem().getItemSeq());
            int requiredQuantity = bomItem.getBomChildItemQuantity() * indicatedQuantity;

            // 품목별 비고 설정 (Request에서 매핑된 비고 가져오기)
            String note = itemNotes.getOrDefault(subItem.getItemSeq(), null);
            if (note == null) {
                log.warn("ItemSeq {}에 대한 비고가 요청에 존재하지 않습니다.", subItem.getItemSeq());
            }
            log.info(note);

            // 생산불출 품목 생성
            ProductionDisbursementItem disbursementItem = productionDisbursementDomainService.createProductionDisbursementItem(
                    newProductionDisbursement,
                    subItem,
                    warehouse,
                    requiredQuantity,
                    note
            );
            log.info("ItemSeq: {}, RequiredQuantity: {}, Note: {}", subItem.getItemSeq(), requiredQuantity, note);

            newProductionDisbursement.addDisbursementItem(disbursementItem); // 품목 추가
            totalDisbursementQuantity += requiredQuantity;
        }
        newProductionDisbursement.specifyProductionDisbursementTotalQuantity(totalDisbursementQuantity);

        // 작업지시서 상태 진행중으로 변경
        workOrderDomainService.updateWorkOrderStatus(workOrder, WorkOrderStatus.ONGOING);

        // 주문서 상태 생산중으로 변경
        salesOrderDomainService.updateSalesOrderStatus(workOrder.getSalesOrder(), "PRODUCTION");

        // 생산불출 저장
        productionDisbursementDomainService.saveProductionDisbursement(newProductionDisbursement);
    }

    // 수정
    @Transactional
    public void updateWorkOrder(Long productionDisbursementSeq, CreateAndUpdateProductionDisbursementRequest request) {
        log.info("-------------- 생산불출 수정 서비스 진입 - {}번 수정, 수정요청 조건 - request: {}   --------------", productionDisbursementSeq, request);

        // 기존 생산불출 조회
        ProductionDisbursement productionDisbursement = productionDisbursementDomainService.findByProductionDisbursementSeq(productionDisbursementSeq);

        // 수정 가능한 상태인지 확인
        productionDisbursementDomainService.checkProductionDisbursementStatus(productionDisbursement.getProductionDisbursementStatus());

        WorkOrder workOrder = workOrderDomainService.findByWorkOrderSeq(request.getWorkOrderSeq());

        // 생산불출일 유효값 검증
        LocalDateTime productionDisbursementDepartureDate = request.getProductionDisbursementDepartureDate();
        if (productionDisbursementDepartureDate == null) {
            throw new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_REQUIRED_INFORMATION);
        }

        // 불출일이 작업지시서 납기일보다 전이어야 함.
        if (productionDisbursementDepartureDate.isAfter(workOrder.getWorkOrderDueDate())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }

        // 불출일이 작업지시서 지시일 이후 이어야 함.
        if (productionDisbursementDepartureDate.isBefore(workOrder.getWorkOrderIndicatedDate())) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }

        // 생산불출일 시간대 변경
        LocalDateTime seoulDepartureDate = productionDisbursementDomainService.convertDate(productionDisbursementDepartureDate);
        request.setProductionDisbursementDepartureDate(seoulDepartureDate);
        log.info("-------------- 불출일 시간대 변경 완료 : {} --------------", seoulDepartureDate);

        // 수정
        productionDisbursementDomainService.updateProductionDisbursement(request, productionDisbursementSeq, workOrder);

        // 품목 변경 여부 확인
        boolean itemChanges = productionDisbursementDomainService.itemChanges(productionDisbursement, request);

        if (itemChanges) {
            // 3-1. 기존 품목 삭제
            List<ProductionDisbursementItem> disbursementItems = productionDisbursementItemRepository.findAllByProductionDisbursement(productionDisbursement);
            productionDisbursementItemRepository.deleteAllInBatch(disbursementItems);

            // 3-2. 새로운 품목 등록
            if (request.getItemRequests() != null && !request.getItemRequests().isEmpty()) {
                for (ProductionDisbursementItemRequest dto : request.getItemRequests()) {
                    Item item = itemRepository.findById(dto.getItemSeq())
                            .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));

                    Warehouse warehouse = warehouseRepository.findById(item.getWarehouse().getWarehouseSeq())
                            .orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));

                    int requiredQuantity = productionDisbursementDomainService.calculateRequiredQuantity(item.getItemSeq(), productionDisbursement.getWorkOrder().getWorkOrderSeq());

                    ProductionDisbursementItem disbursementItem = ProductionDisbursementItem.createProductionDisbursementItem(
                            productionDisbursement, item, warehouse, requiredQuantity, dto.getNote()
                    );
                    productionDisbursementItemRepository.save(disbursementItem);
                }
            }
        }

    }

    // 삭제
    @Transactional
    public void deleteProductionDisbursement(Long productionDisbursementSeq) {
        log.info("-------------- 생산불출 삭제 서비스 진입 - {}번 삭제 --------------", productionDisbursementSeq);

        // 작업지시서 찾기
        ProductionDisbursement productionDisbursement = productionDisbursementDomainService.findByProductionDisbursementSeq(productionDisbursementSeq);

        // 삭제가능한 상태인지 체크
        productionDisbursementDomainService.checkProductionDisbursementStatus(productionDisbursement.getProductionDisbursementStatus());

        // 연결된 작업지시서 조회
        WorkOrder workOrder = productionDisbursement.getWorkOrder();
        if (workOrder == null) {
            throw new CustomException(ErrorCodeType.WORK_ORDER_NOT_FOUND);
        }

        // 생산불출 품목 삭제
        List<ProductionDisbursementItem> disbursementItems = productionDisbursementItemRepository.findAllByProductionDisbursement(productionDisbursement);
        productionDisbursementItemRepository.deleteAllInBatch(disbursementItems);

        // 삭제로 상태 변경
        productionDisbursementDomainService.deleteProductionDisbursement(productionDisbursement);

        // 작업지시서 상태 after 로 변경
        workOrderDomainService.updateWorkOrderStatus(workOrder, WorkOrderStatus.AFTER);

        // 주문서 상태 after 로 변경
        salesOrderDomainService.updateSalesOrderStatus(workOrder.getSalesOrder(), "AFTER");
        log.info("-------------- 생산불출 삭제 및 작업지시서, 주문서 상태 변경 완료 --------------");
    }

    // 불출 상태 변경
    @Transactional
    public void updateProductionDisbursementStatus(Long productionDisbursementSeq, ProductionDisbursementStatus newStatus) {
        log.info("-------------- 생산불출 상태 변경 서비스 진입 - {}번 변경 --------------", productionDisbursementSeq);
        // 생산불출 찾기
        ProductionDisbursement productionDisbursement = productionDisbursementDomainService.findByProductionDisbursementSeq(productionDisbursementSeq);
        ProductionDisbursementStatus currentStatus  = productionDisbursement.getProductionDisbursementStatus();

        if (!currentStatus.equals(ProductionDisbursementStatus.BEFORE)) {
            throw new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_STATE_BAD_REQUEST);
        }

        // 상태 변경
        productionDisbursementDomainService.updateProductionDisbursementStatus(productionDisbursement, newStatus);
        log.info("-------------- 생산불출 상태 변경 완료 - {}번, 새로운 상태: {} --------------", productionDisbursementSeq, newStatus);
    }
}
