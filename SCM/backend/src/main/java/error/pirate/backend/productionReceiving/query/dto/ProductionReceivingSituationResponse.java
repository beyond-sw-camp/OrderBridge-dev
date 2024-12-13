package error.pirate.backend.productionReceiving.query.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductionReceivingSituationResponse {
    private LocalDateTime productionReceivingRegDate;
    private String productionReceivingRegMonth;
    private Integer productionReceivingExtendedPrice;
    private String productionReceivingName;
    private String clientName;
    private String productionReceivingNote;
    private Integer productionReceivingSum;
}
