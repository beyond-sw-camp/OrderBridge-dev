package error.pirate.backend.shippingInstruction.query.dto;

import lombok.Getter;

@Getter
public class ShippingInstructionItemCheckDTO {
    public Long item;
    public Integer remainingQuantity;
}
