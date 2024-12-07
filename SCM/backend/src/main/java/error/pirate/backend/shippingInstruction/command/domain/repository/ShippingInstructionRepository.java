package error.pirate.backend.shippingInstruction.command.domain.repository;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;

import java.time.LocalDateTime;

public interface ShippingInstructionRepository {
    ShippingInstruction save(ShippingInstruction newShippingInstruction);

    long countByShippingInstructionRegDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
