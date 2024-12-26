package error.pirate.backend.purchase.query.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseSituationResponse {

    private Long purchaseSeq;

    private String purchaseName; // 구매서명

    private LocalDateTime purchaseRegDate; //  일자

    private LocalDateTime purchaseContractDate; //  구매 계약일

    private Integer purchaseExtendedPrice;  //  품목 총금액

    private Integer purchaseTotalQuantity;

    private Integer purchaseMonthPrice;  //  월별 총금액

    private Integer purchaseMonthQuantity;   //  월별 총수량

    private String purchaseRegMonth;

    private String purchaseNote; 

}
