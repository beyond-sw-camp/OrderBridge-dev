package error.pirate.backend.salesOrder.query.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListDTO;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.query.dto.SalesOrderDTO;
import error.pirate.backend.salesOrder.query.repository.SalesOrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static error.pirate.backend.productionReceiving.command.domain.aggregate.entity.QProductionReceiving.productionReceiving;
import static error.pirate.backend.salesOrder.command.domain.aggregate.entity.QSalesOrder.salesOrder;
import static error.pirate.backend.workOrder.command.domain.aggregate.entity.QWorkOrder.workOrder;

@Repository
@RequiredArgsConstructor
public class SalesOrderQueryRepositoryImpl implements SalesOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public SalesOrder findByProductionReceivingSeq(Long productionReceivingSeq) {
        return queryFactory
                .selectFrom(salesOrder)
                .leftJoin(workOrder).on(workOrder.salesOrder.salesOrderSeq.eq(salesOrder.salesOrderSeq))
                .leftJoin(productionReceiving).on(productionReceiving.workOrder.eq(workOrder))
                .where(productionReceiving.productionReceivingSeq.eq(productionReceivingSeq))
                .fetchOne();
    }
}
