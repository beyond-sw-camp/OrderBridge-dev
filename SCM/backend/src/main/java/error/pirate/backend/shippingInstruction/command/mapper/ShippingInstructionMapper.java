package error.pirate.backend.shippingInstruction.command.mapper;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.user.command.domain.aggregate.entity.User;

public class ShippingInstructionMapper {
    public static ShippingInstruction toEntity(ShippingInstructionRequest shippingInstructionRequest, SalesOrder salesOrder, User user, String shippingInstructionName) {
        return ShippingInstruction.create(
                salesOrder,
                user,
                shippingInstructionName,
                shippingInstructionRequest.getShippingInstructionAddress(),
                "결재전",
                shippingInstructionRequest.getShippingInstructionScheduledShipmentDate(),
                shippingInstructionRequest.getItemTotalQuantity(),
                shippingInstructionRequest.getShippingInstructionNote()
        );
    }
}
