package error.pirate.backend.purchase.query.dto;

import error.pirate.backend.common.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder @Getter
public class PurchaseResponsePagination {

    private List<PurchaseResponse> purchaseResponseList;

    private Pagination pagination;

}
