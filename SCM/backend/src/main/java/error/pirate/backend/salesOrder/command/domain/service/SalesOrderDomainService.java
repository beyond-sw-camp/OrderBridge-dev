package error.pirate.backend.salesOrder.command.domain.service;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOrderDomainService {
    
    private final SalesOrderRepository salesOrderRepository;

    public SalesOrder findBySalesOrderName(String salesOrderName) {
        return salesOrderRepository.findBySalesOrderName(salesOrderName);
    }
}
