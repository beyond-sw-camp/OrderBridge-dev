package error.pirate.backend.productionDisbursement.command.domain.aggregate.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.BomItemRepository;
import error.pirate.backend.productionDisbursement.command.application.dto.CreateAndUpdateProductionDisbursementRequest;
import error.pirate.backend.productionDisbursement.command.application.dto.ProductionDisbursementItemRequest;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementItem;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.repository.ProductionDisbursementItemRepository;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.repository.ProductionDisbursementRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionDisbursementDomainService {

    private final ProductionDisbursementRepository productionDisbursementRepository;
    private final BomItemRepository bomItemRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ProductionDisbursementItemRepository productionDisbursementItemRepository;

    // 생산불출 번호로 불출서 조회
    public ProductionDisbursement findByProductionDisbursementSeq(Long productionDisbursementSeq) {
        return productionDisbursementRepository.findById(productionDisbursementSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_NOT_FOUND));
    }

    // 삭제 가능한 상태 확인 - 전일때만 삭제 가능
    public void checkProductionDisbursementStatus(ProductionDisbursementStatus productionDisbursementStatus) {
        if (!(productionDisbursementStatus.equals(ProductionDisbursementStatus.BEFORE))) {
            throw new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_STATE_BAD_REQUEST);
        }
    }

    // 생산불출 삭제(상태로 변경)
    public void deleteProductionDisbursement(ProductionDisbursement productionDisbursement) {
        // 상태 수정을 위해 엔터티 변경
        productionDisbursement.deleteProductionDisbursement();
    }

    // 작업지시서 중복체크
    public boolean existsByWorkOrderSeq(Long workOrderSeq) {
        return productionDisbursementRepository.existsByWorkOrderSeqAndProductionDisbursementStatus(workOrderSeq);
    }

    // 불출일 서울시간으로 변경
    public LocalDateTime convertDate(LocalDateTime productionDisbursementDepartureDate) {
        ZonedDateTime systemZonedDateTime = productionDisbursementDepartureDate.atZone(ZoneId.systemDefault());
        ZonedDateTime seoulZonedDateTime = systemZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return seoulZonedDateTime.toLocalDateTime();
    }

    // 생산불출 생성
    public ProductionDisbursement createProductionDisbursement(CreateAndUpdateProductionDisbursementRequest request,
                                                               WorkOrder workOrder,
                                                               User user,
                                                               String productionDisbursementName) {
        return ProductionDisbursement.createProductionDisbursement(request, workOrder, user, productionDisbursementName);
    }

    // 생산불출 품목 생성
    public ProductionDisbursementItem createProductionDisbursementItem(ProductionDisbursement newProductionDisbursement,
                                                                       Item subItem,
                                                                       Warehouse warehouse,
                                                                       int requiredQuantity,
                                                                       String note) {
        return ProductionDisbursementItem.createProductionDisbursementItem(
                newProductionDisbursement, subItem, warehouse, requiredQuantity, note
        );
    }

    // 생산불출 저장
    public void saveProductionDisbursement(ProductionDisbursement newProductionDisbursement) {
        productionDisbursementRepository.save(newProductionDisbursement);
    }

    public int calculateRequiredQuantity(Long itemSeq, Long workOrderSeq) {
        // 작업지시서에서 작업 수량 가져오기
        WorkOrder workOrder = workOrderRepository.findById(workOrderSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.WORK_ORDER_NOT_FOUND));

        // BOM에서 자식 품목의 수량 가져오기
        BomItem bomItems = bomItemRepository.findByChildItem_ItemSeq(itemSeq, workOrder.getItem().getItemSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));

        return bomItems.getBomChildItemQuantity() * workOrder.getWorkOrderIndicatedQuantity();
    }

    public boolean itemChanges(ProductionDisbursement productionDisbursement, CreateAndUpdateProductionDisbursementRequest request) {
        // 기존 품목 목록 조회
        List<ProductionDisbursementItem> existingItems = productionDisbursementItemRepository.findAllByProductionDisbursement(productionDisbursement);
        List<ProductionDisbursementItemRequest> newItems = request.getItemRequests();

        // 1. 기존 품목 수량과 새로운 품목 수량 비교
        if (existingItems.size() != newItems.size()) {
            return true; // 수량이 다르면 변경된 것으로 판단
        }

        // 2. 각 항목별로 비교
        for (int i = 0; i < existingItems.size(); i++) {
            ProductionDisbursementItem existingItem = existingItems.get(i);
            ProductionDisbursementItemRequest newItem = newItems.get(i);

            // 새로운 수량은 BOM과 작업지시서 수량을 기반으로 계산
            int calculatedRequiredQuantity = calculateRequiredQuantity(newItem.getItemSeq(), productionDisbursement.getWorkOrder().getWorkOrderSeq());

            if (!existingItem.getItem().getItemSeq().equals(newItem.getItemSeq()) ||
                    !existingItem.getProductionDisbursementQuantity().equals(calculatedRequiredQuantity) ||
                    !Objects.equals(existingItem.getProductionDisbursementNote(), newItem.getNote())) {
                return true; // 항목 중 하나라도 다르면 변경된 것으로 판단
            }
        }

        // 모든 항목이 동일하면 변경되지 않은 것으로 판단
        return false;
    }
}
