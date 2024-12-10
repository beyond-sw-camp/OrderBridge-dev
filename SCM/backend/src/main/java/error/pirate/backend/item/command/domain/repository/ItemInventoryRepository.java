package error.pirate.backend.item.command.domain.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
    List<ItemInventory> findAllByItem(Item item);
}
