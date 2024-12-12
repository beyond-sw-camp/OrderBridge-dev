package error.pirate.backend.salesOrder.command.domain.service;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesOrderItemDomainService {

    private final SalesOrderItemRepository salesOrderItemRepository;

    public List<SalesOrderItem> findAllBySalesOrderSeq(Long salesOrderSeq) {
        return salesOrderItemRepository.findAllBySalesOrderSeq(salesOrderSeq);
    }
}
