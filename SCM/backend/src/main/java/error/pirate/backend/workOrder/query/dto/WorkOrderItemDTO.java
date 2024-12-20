package error.pirate.backend.workOrder.query.dto;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
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
    private Integer itemPrice;
    private ItemUnit itemUnitTitle;
    private ItemDivision itemDivision;
    private String itemNote;

}
