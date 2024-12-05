package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "품목 DTO (임시)")
public class ItemDTO {
    private String itemName;
    private String itemDivision;
    private int itemPrice;

    private int shippingInstructionItemQuantity;
    private String shippingInstructionItemNote;

    private int itemTotalQuantity;
}
