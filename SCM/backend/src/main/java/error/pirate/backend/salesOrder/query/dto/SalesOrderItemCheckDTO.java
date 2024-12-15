package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;

@Getter
public class SalesOrderItemCheckDTO {
    public Long quotationItemSeq;
    public Integer remainingQuantity;
}
