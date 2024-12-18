package error.pirate.backend.purchase.query.dto;

import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PurchaseResponse {

    private Long purchaseSeq;

    private Long purchaseOrderSeq;

    private String userName; // 회원 이름

    private String clientName; // 거래처 이름

    private String purchaseName; // 구매처명

    private PurchaseStatus purchaseStatus; // 상태

    @Setter
    private String purchaseStatusValue;

    private LocalDateTime purchaseContractDate; // 계약일

    private LocalDateTime purchaseRegDate;

    private LocalDateTime purchaseModDate;

    private Integer purchaseExtendedPrice; // 구매서 총금액

    private String purchaseNote; // 구매서 비고

    @Setter
    private List<PurchaseItemResponse> purchaseItemResponseList;

}
