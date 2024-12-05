package error.pirate.backend.shippingInstruction.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_shipping_instruction_item") // 출하 지시서 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingInstructionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shippingInstructionItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingInstructionSeq")
    private ShippingInstruction shippingInstruction; // 출하 지시서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int shippingInstructionItemQuantity; // 출하 지시서 수량

    private String shippingInstructionItemNote; // 출하 지시서 품목 비고
}