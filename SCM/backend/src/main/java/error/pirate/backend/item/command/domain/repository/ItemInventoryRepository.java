package error.pirate.backend.item.command.domain.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
    List<ItemInventory> findAllByItemAndItemInventoryRemainAmountGreaterThanOrderByItemInventoryExpirationDate(Item item, Integer itemInventoryRemainAmount);

    int countByItemAndItemInventoryExpirationDateAfter(Item item, LocalDateTime itemInventoryExpirationDate);

}
