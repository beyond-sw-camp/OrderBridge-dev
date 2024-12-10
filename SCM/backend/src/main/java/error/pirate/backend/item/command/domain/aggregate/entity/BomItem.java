package error.pirate.backend.item.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_bom_item") // BOM 품목
@Getter
@NoArgsConstructor
public class BomItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bomItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentItemSeq")
    private Item parentItem; // 상위 품목

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "childItemSeq")
    private Item childItem; // 하위 품목

    private Integer bomChildItemQuantity; // 하위 품목 수량

    public static BomItem createBomItem(Item parentItem, Item childItem, Integer bomChildItemQuantity) {
        BomItem bomItem = new BomItem(bomChildItemQuantity);
        bomItem.specifyParentItem(parentItem);
        bomItem.specifyChildItem(childItem);

        return bomItem;
    }

    protected BomItem(Integer bomChildItemQuantity) {
        this.bomChildItemQuantity = bomChildItemQuantity;
    }

    private void specifyParentItem(Item parentItem) {
        this.parentItem = parentItem;
    }

    private void specifyChildItem(Item childItem) {
        this.childItem = childItem;
    }
}
