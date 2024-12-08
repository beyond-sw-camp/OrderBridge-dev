package error.pirate.backend.shippingSlip.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_shipping_slip_item") // 출하전표 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingSlipItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingSlipItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingSlipSeq")
    private ShippingSlip shippingSlip; // 출하전표

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int shippingSlipItemQuantity; // 출하전표 품목 수량

    private String shippingSlipItemNote; // 찰하전표 품목 비고
}
