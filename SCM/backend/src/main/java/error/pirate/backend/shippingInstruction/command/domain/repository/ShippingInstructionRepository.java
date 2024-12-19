package error.pirate.backend.shippingInstruction.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingInstructionRepository extends JpaRepository<ShippingInstruction, Long> {
    List<ShippingInstruction> findBySalesOrder(SalesOrder salesOrder);
}
