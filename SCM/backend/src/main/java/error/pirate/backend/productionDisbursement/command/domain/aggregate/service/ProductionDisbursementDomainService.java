package error.pirate.backend.productionDisbursement.command.domain.aggregate.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.repository.ProductionDisbursementRepository;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
