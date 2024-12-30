package error.pirate.backend.shippingSlip.query.dto;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingAddress;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ShippingSlipExcelDTO {
    private LocalDateTime shippingSlipRegDate;
    private LocalDateTime shippingSlipModDate;
    private String shippingSlipName;
    private LocalDate shippingSlipShippingDate;
    private Integer shippingSlipTotalQuantity;
    private ShippingAddress shippingSlipAddress;
    private String shippingSlipNote;
}
