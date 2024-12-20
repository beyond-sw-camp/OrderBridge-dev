package error.pirate.backend.shippingSlip.command.mapper;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.user.command.domain.aggregate.entity.User;

import java.time.LocalDateTime;

public class ShippingSlipMapper {
    public static ShippingSlip toEntity(
            ShippingSlipRequest shippingSlipRequest, ShippingInstruction shippingInstruction,
            User user, String shippingSlipName,
            LocalDateTime shippingSlipShippingDate, int itemTotalQuantity) {
        return ShippingSlip.create(
                shippingInstruction,
                user,
                shippingSlipName,
                shippingInstruction.getShippingAddress(),
                shippingSlipShippingDate,
                itemTotalQuantity,
                shippingSlipRequest.getShippingSlipNote()
        );
    }
}
