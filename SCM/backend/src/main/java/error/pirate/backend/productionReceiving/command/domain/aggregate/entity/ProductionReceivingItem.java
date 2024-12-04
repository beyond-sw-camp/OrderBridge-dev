package error.pirate.backend.productionReceiving.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_production_receiving_item") // 생산 입고 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductionReceivingItem {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productionReceivingItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "salesOrderSeq")
    private SalesOrder salesOrder; // 생산 입고

    private int productionReceivingItemQuantity; // 생산 입고 품목 수량

    private int productionReceivingItemUnitPrice; // 생산 단가

    private String productionReceivingItemNote; // 생산 입고 품목 비고
}
