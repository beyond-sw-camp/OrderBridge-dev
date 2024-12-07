package error.pirate.backend.shippingInstruction.command.infrastructure.repository;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.repository.ShippingInstructionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaShippingInstructionRepository extends ShippingInstructionRepository, JpaRepository<ShippingInstruction, Long> {
}
