package error.pirate.backend.shippingSlip.command.domain.repository;

import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ShippingSlipRepository extends JpaRepository<ShippingSlip, Long> {
    long countByShippingSlipRegDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
