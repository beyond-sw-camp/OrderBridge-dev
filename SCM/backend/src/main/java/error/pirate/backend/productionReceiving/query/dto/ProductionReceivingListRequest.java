package error.pirate.backend.productionReceiving.query.dto;

import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductionReceivingListRequest {
    private Long userSeq;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate searchStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate searchEndDate;
    private String searchName;
    private ProductionReceivingStatus searchStatus;
}
