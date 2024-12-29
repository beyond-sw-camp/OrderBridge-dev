package error.pirate.backend.productionDisbursement.query.dto;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "생산불출 품목 DTO")
public class ProductionDisbursementItemDTO {

    private Long productionDisbursementItemSeq;
    private Long itemSeq;
    private String itemName;
    private String itemImageUrl;
    private ItemUnit itemUnitTitle;
    private ItemDivision itemDivision;
    private Long storeSeq;
    private String storeName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime itemInventoryExpirationDate;
    private Integer productionDisbursementQuantity;
    private String productionDisbursementNote;

}
