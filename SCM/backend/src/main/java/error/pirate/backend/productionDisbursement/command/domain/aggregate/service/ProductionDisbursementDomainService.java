package error.pirate.backend.productionDisbursement.command.domain.aggregate.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.productionDisbursement.command.application.dto.CreateProductionDisbursementRequest;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementItem;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.repository.ProductionDisbursementRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionDisbursementDomainService {

    private final ProductionDisbursementRepository productionDisbursementRepository;

    // 생산불출 번호로 불출서 조회
    public ProductionDisbursement findByProductionDisbursementSeq(Long productionDisbursementSeq) {
        return productionDisbursementRepository.findById(productionDisbursementSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_NOT_FOUND));
    }

    // 삭제 가능한 상태 확인 - 전일때만 삭제 가능
    public void checkProductionDisbursementStatusDeletePossible(ProductionDisbursementStatus productionDisbursementStatus) {
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
    public ProductionDisbursement createProductionDisbursement(CreateProductionDisbursementRequest request,
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
}
