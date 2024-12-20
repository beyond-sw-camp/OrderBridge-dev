package error.pirate.backend.productionReceiving.query.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListDTO;
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

@Repository
@RequiredArgsConstructor
public class ProductionReceivingQueryRepositoryImpl implements ProductionReceivingQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductionReceivingListDTO> findAllByFilter(ProductionReceivingListRequest request, Pageable pageable) {

        List<ProductionReceivingListDTO> results = queryFactory
                .select(Projections.constructor(ProductionReceivingListDTO.class,
                        productionReceiving.productionReceivingSeq,
                        productionReceiving.productionReceivingName,
                        productionReceiving.productionReceivingRegDate,
                        productionReceiving.productionReceivingStatus,
                        productionReceiving.productionReceivingReceiptDate
                ))
                .from(productionReceiving)
                .where(productReceivingNameEq(request.getSearchName()),
                        productReceivingStatusIn(request.getSearchStatus()),
                        productReceivingRegDateGoeLoe(request.getSearchStartDate(), request.getSearchEndDate()),
                        productionReceiving.productionReceivingStatus.ne(ProductionReceivingStatus.DELETE))
                .orderBy(productionReceiving.productionReceivingSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(productionReceiving.count())
                .from(productionReceiving)
                .where(productReceivingNameEq(request.getSearchName()),
                        productReceivingStatusIn(request.getSearchStatus()),
                        productReceivingRegDateGoeLoe(request.getSearchStartDate(), request.getSearchEndDate()),
                        productionReceiving.productionReceivingStatus.ne(ProductionReceivingStatus.DELETE))
                .fetchOne();

        return new PageImpl<>(results, pageable, count == null ? 0 : count);
    }

    private BooleanExpression userSeqEq(Long userSeq) {
        return userSeq == null ? null : productionReceiving.user.userSeq.eq(userSeq);
    }

    private BooleanExpression productReceivingNameEq(String searchName) {
        return StringUtils.isBlank(searchName) ? null : productionReceiving.productionReceivingName.eq(searchName);
    }

    private BooleanExpression productReceivingStatusIn(ProductionReceivingStatus[] searchStatus) {
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
