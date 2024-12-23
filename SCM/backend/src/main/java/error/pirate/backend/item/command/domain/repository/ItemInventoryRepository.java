package error.pirate.backend.item.command.domain.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
    List<ItemInventory> findAllByItemAndItemInventoryRemainAmountGreaterThanOrderByItemInventoryExpirationDate(Item item, Integer itemInventoryRemainAmount);

    int countByItemAndItemInventoryExpirationDateAfter(Item item, LocalDateTime itemInventoryExpirationDate);

    @Query("SELECT COALESCE(SUM(ii.itemInventoryRemainAmount), 0) " +
            "FROM ItemInventory ii " +
            "WHERE ii.item = :item AND ii.itemInventoryExpirationDate > :expirationDate")
    int sumRemainAmountByItemAndItemInventoryExpirationDateAfter(
            @Param("item") Item item,
            @Param("expirationDate") LocalDateTime expirationDate
    );
}
