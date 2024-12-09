package error.pirate.backend.salesOrder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SalesOrderSituationResponse {
    private List<SalesOrderSituationDTO> salesOrderSituationList;
}
