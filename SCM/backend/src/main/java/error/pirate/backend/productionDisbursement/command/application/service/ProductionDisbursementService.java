package error.pirate.backend.productionDisbursement.command.application.service;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.repository.ProductionDisbursementRepository;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.service.ProductionDisbursementDomainService;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionDisbursementService {

    private final ProductionDisbursementDomainService productionDisbursementDomainService;



    @Transactional
    public void deleteProductionDisbursement(Long productionDisbursementSeq) {
        log.info("-------------- 생산불출 삭제 서비스 진입 - {}번 삭제 --------------", productionDisbursementSeq);

        // 작업지시서 찾기
        ProductionDisbursement productionDisbursement = productionDisbursementDomainService.findByProductionDisbursementSeq(productionDisbursementSeq);

        // 삭제가능한 상태인지 체크
        productionDisbursementDomainService.checkProductionDisbursementStatusDeletePossible(productionDisbursement.getProductionDisbursementStatus());

        // 삭제로 상태 변경
        productionDisbursementDomainService.deleteProductionDisbursement(productionDisbursement);
    }
}
