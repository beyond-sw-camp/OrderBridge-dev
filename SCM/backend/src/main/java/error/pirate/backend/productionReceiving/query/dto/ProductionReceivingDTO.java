package error.pirate.backend.productionReceiving.query.dto;

import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductionReceivingDTO {
    private Long productionReceivingSeq;
    private String productionReceivingName; // 생산 입고명
    private Integer productionReceivingExtendedPrice; // 생산 입고 총금액
    private String productionReceivingNote; // 생산 입고 비고
    private ProductionReceivingStatus productionReceivingStatus;
    private LocalDateTime productionReceivingRegDate;
}
