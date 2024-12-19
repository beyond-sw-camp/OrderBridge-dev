package error.pirate.backend.productionReceiving.query.repository.impl;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingItemQueryDTO;
import error.pirate.backend.productionReceiving.query.repository.ProductionReceivingItemQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static error.pirate.backend.item.command.domain.aggregate.entity.QItem.item;
import static error.pirate.backend.productionReceiving.command.domain.aggregate.entity.QProductionReceivingItem.productionReceivingItem;
import static error.pirate.backend.warehouse.command.domain.aggregate.entity.QWarehouse.warehouse;

@Repository
@RequiredArgsConstructor
public class ProductionReceivingItemQueryRepositoryImpl implements ProductionReceivingItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductionReceivingItemQueryDTO> findAllByProductionReceivingSeq(Long productionReceivingSeq) {
        return queryFactory
                .select(Projections.constructor(ProductionReceivingItemQueryDTO.class,
                        productionReceivingItem.productionReceivingItemSeq,
                        item.itemSeq,
                        item.itemName,
                        item.itemImageUrl,
                        warehouse.warehouseSeq,
                        warehouse.warehouseName,
                        productionReceivingItem.productionReceivingItemQuantity,
                        productionReceivingItem.productionReceivingUnitPrice,
                        productionReceivingItem.productionReceivingItemNote))
                .from(productionReceivingItem)
                .leftJoin(productionReceivingItem.item, item)
                .leftJoin(productionReceivingItem.warehouse, warehouse)
                .where(productionReceivingItem.productionReceiving.productionReceivingSeq.eq(productionReceivingSeq))
                .fetch();
    }
}
