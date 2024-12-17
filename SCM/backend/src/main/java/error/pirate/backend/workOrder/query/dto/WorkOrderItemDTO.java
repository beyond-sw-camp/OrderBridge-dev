package error.pirate.backend.workOrder.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "작업지시서 품목 DTO")
public class WorkOrderItemDTO {

    private Long itemSeq;
    private String itemName;
    private String itemImageUrl;
    private String itemPrice;
    private String itemUnitTitle;
    private String itemDivision;
    private String itemNote;

}
