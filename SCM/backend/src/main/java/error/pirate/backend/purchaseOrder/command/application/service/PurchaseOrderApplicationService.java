package error.pirate.backend.purchaseOrder.command.application.service;

import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderApplicationService {

    private final PurchaseOrderDomainService purchaseOrderDomainService;


}
