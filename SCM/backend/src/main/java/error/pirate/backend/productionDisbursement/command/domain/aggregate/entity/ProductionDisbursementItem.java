package error.pirate.backend.productionDisbursement.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_production_disbursement_item") // 생산불출 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductionDisbursementItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productionDisbursementItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "productionDisbursementSeq")
    private ProductionDisbursement productionDisbursement; // 생산불출

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredientsWarehouseSeq")
    private Warehouse ingredientsWarehouse; // 원자재 창고

    private Integer productionDisbursementQuantity; // 생산불출 품목 수량

    private String productionDisbursementNote; // 생산불출 품목 비고


    // 생성 메소드
    public static ProductionDisbursementItem createProductionDisbursementItem(ProductionDisbursement newProductionDisbursement,
                                                                              Item subItem,
                                                                              Warehouse warehouse,
                                                                              int requiredQuantity,
                                                                              String note) {
        ProductionDisbursementItem item = new ProductionDisbursementItem();
        item.productionDisbursement = newProductionDisbursement;
        item.item = subItem;
        item.ingredientsWarehouse = warehouse;
        item.productionDisbursementQuantity = requiredQuantity;
        item.productionDisbursementNote = note;

        return item;
    }

    public void specifyProductionDisbursement(ProductionDisbursement productionDisbursement) {
        this.productionDisbursement = productionDisbursement;
    }
}
