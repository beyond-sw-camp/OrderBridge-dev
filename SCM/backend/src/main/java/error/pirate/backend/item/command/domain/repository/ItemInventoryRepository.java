package error.pirate.backend.item.command.domain.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
}
