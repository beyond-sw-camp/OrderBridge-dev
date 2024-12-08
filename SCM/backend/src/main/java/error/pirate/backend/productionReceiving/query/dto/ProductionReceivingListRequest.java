package error.pirate.backend.productionReceiving.query.dto;

import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionReceivingListRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate searchStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate searchEndDate;
    private String searchName;
    private ProductionReceivingStatus searchStatus;
}
