package error.pirate.backend.purchaseOrder.command.domain.service;

import error.pirate.backend.purchaseOrder.command.domain.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderDomainService {

    private final PurchaseOrderRepository purchaseOrderRepository;


}
