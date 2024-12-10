package error.pirate.backend.purchaseOrder.query.dto;

import error.pirate.backend.common.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder @Getter
public class PurchaseOrderResponsePagination {

    private List<PurchaseOrderResponse> purchaseOrderResponseList;

    private Pagination pagination;

}
