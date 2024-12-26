package error.pirate.backend.shippingSlip.command.domain.repository;

import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingSlipRepository extends JpaRepository<ShippingSlip, Long> {

    List<ShippingSlip> findByShippingInstructionShippingInstructionSeq(long shippingInstructionSeq);
}
