package error.pirate.backend.productionReceiving.query.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingDTO;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import error.pirate.backend.productionReceiving.query.repository.ProductionReceivingQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static error.pirate.backend.productionReceiving.command.domain.aggregate.entity.QProductionReceiving;

@Repository
@RequiredArgsConstructor
public class ProductionReceivingQueryRepositoryImpl implements ProductionReceivingQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductionReceivingDTO> findAllByFilter(ProductionReceivingListRequest request, Pageable pageable) {

        QWarehouse productionWarehouse = new QWarehouse("productionWarehouse");
        QWarehouse storeWarehouse = new QWarehouse("storeWarehouse");

        List<ProductionReceivingDTO> results = queryFactory
                .select(Projections.fields(ProductionReceivingDTO.class,
                        productionReceiving.productionReceivingName,
                        productionReceiving.productionReceivingRegDate,
                        productionReceiving.productionReceivingStatus,
                        productionWarehouse.warehouseName,
                        storeWarehouse.warehouseName
                ))
                .from(productionReceiving)
                .leftJoin(user, productionReceiving.user)
                .leftJoin(productionWarehouse, productionReceiving.productionWarehouse)
                .leftJoin(storeWarehouse, productionReceiving.productionWarehouse)
                .where(productReceivingNameEq(request.getSearchName()),
                        productReceivingStatusIn(request.getSearchStatus()),
                        productReceivingRegDateGoeLoe(request.getSearchStartDate(), request.getSearchEndDate()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression productReceivingNameEq(String searchName) {
        return productReceivingName == null ? null : productReceiving.productReceivingName.eq(searchName);
    }

    private BooleanExpression productReceivingStatusIn(ProductionReceivingStatus searchStatus) {
        return searchStatus == null ? null : productReceiving.productReceivingStatus.in(searchStatus);
    }

    // 날짜 필터
    private BooleanExpression productReceivingRegDateGoeLoe(LocalDate searchStartDate, LocalDate searchEndDate) {

        //goe, loe 사용
        BooleanExpression isGoeStartDate = searchStartDate == null ? null : productReceiving.productReceivingRegDate.goe(LocalDateTime.of(searchStartDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = searchEndDate == null ? null : productReceiving.productReceivingRegDate.loe(LocalDateTime.of(searchEndDate, LocalTime.MAX).withNano(0));

        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }
}
