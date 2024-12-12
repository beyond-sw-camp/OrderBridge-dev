package error.pirate.backend.shippingSlip.command.domain.repository;

import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlipItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingSlipItemRepository extends JpaRepository<ShippingSlipItem, Long> {
    void deleteByShippingSlip(ShippingSlip newShippingSlip);
}
