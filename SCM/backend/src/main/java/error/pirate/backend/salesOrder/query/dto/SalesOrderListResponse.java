package error.pirate.backend.salesOrder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SalesOrderListResponse {
    private List<SalesOrderListItemDTO> salesOrder;
    private int currentPage;
    private int totalPages;
    private int totalSalesOrder;
}
