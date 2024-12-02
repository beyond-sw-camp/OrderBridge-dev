package error.pirate.backend.item.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_item_unit")
@Getter
@NoArgsConstructor
public class ItemUnit {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemUnitSeq;

    private String itemUnitTitle; // 품목 단위명
}
