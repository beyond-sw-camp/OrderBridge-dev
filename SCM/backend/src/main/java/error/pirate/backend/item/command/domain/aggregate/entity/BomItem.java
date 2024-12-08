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

    // OneToOne 하면 재료가 하나로 고정되는 것 아님?
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentItemSeq")
    private Item parentItem; // 상위 품목

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "childItemSeq")
    private Item childItem; // 하위 품목

    private int bomChildItemQuantity; // 하위 품목 수량
}
