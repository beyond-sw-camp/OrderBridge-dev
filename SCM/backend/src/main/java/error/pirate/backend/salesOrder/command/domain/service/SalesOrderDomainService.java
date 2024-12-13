package error.pirate.backend.salesOrder.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
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

    /* 상태를 수정하는 로직 */
    public void updateSalesOrderStatus(SalesOrder salesOrder, String status) {
        /* 수정을 위해 엔터티 정보 변경 */
        salesOrder.updateStatus(status);
    }
}
