package error.pirate.backend.shippingInstruction.query.dto;

import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationStatus;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingAddress;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ShippingInstructionExcelDTO {
    private LocalDateTime shippingInstructionRegDate;
    private LocalDateTime shippingInstructionModDate;
    private String shippingInstructionName;
    private ShippingInstructionStatus shippingInstructionStatus;
    private LocalDate shippingInstructionScheduledShipmentDate;
    private Integer shippingInstructionTotalQuantity;
    private ShippingAddress shippingInstructionAddress;
    private String shippingInstructionNote;
}
