package error.pirate.backend.productionReceiving.query.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingDTO;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import error.pirate.backend.productionReceiving.query.repository.ProductionReceivingQueryRepository;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.QWarehouse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static error.pirate.backend.productionReceiving.command.domain.aggregate.entity.QProductionReceiving.productionReceiving;
import static error.pirate.backend.user.command.domain.aggregate.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ProductionReceivingQueryRepositoryImpl implements ProductionReceivingQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductionReceivingDTO> findAllByFilter(ProductionReceivingListRequest request, Pageable pageable) {

        QWarehouse productionWarehouse = new QWarehouse("productionWarehouse");
        QWarehouse storeWarehouse = new QWarehouse("storeWarehouse");

        List<ProductionReceivingDTO> results = queryFactory
                .select(Projections.constructor(ProductionReceivingDTO.class,
                        productionReceiving.productionReceivingName,
                        productionReceiving.productionReceivingRegDate,
                        productionReceiving.productionReceivingStatus,
                        productionWarehouse.warehouseName.as("productionWarehouse"),
                        storeWarehouse.warehouseName.as("storeWarehouse")
                ))
                .from(productionReceiving)
                .leftJoin(productionReceiving.user, user)
                .leftJoin(productionReceiving.productionWarehouse, productionWarehouse)
                .leftJoin(productionReceiving.storeWarehouse, storeWarehouse)
                .where(userSeqEq(request.getUserSeq()),
                        productReceivingNameEq(request.getSearchName()),
                        productReceivingStatusIn(request.getSearchStatus()),
                        productReceivingRegDateGoeLoe(request.getSearchStartDate(), request.getSearchEndDate()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression userSeqEq(Long userSeq) {
        return userSeq == null ? null : productionReceiving.user.userSeq.eq(userSeq);
    }

    private BooleanExpression productReceivingNameEq(String searchName) {
        return StringUtils.isBlank(searchName) ? null : productionReceiving.productionReceivingName.eq(searchName);
    }

    private BooleanExpression productReceivingStatusIn(ProductionReceivingStatus searchStatus) {
        return searchStatus == null ? null : productionReceiving.productionReceivingStatus.in(searchStatus);
    }

    // 날짜 필터
    private BooleanExpression productReceivingRegDateGoeLoe(LocalDate searchStartDate, LocalDate searchEndDate) {

        //goe, loe 사용
        BooleanExpression isGoeStartDate = searchStartDate == null ? null : productionReceiving.productionReceivingRegDate.goe(LocalDateTime.of(searchStartDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = searchEndDate == null ? null : productionReceiving.productionReceivingRegDate.loe(LocalDateTime.of(searchEndDate, LocalTime.MAX).withNano(0));

        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }
}
