package error.pirate.backend.item.command.domain.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
    List<ItemInventory> findAllByItemAndItemInventoryRemainAmountGreaterThanOrderByItemInventoryExpirationDate(Item item, Integer itemInventoryRemainAmount);

    int countByItemAndItemInventoryExpirationDateAfter(Item item, LocalDateTime itemInventoryExpirationDate);

    // item_seq와 유효기간 기준으로 정렬된 재고 조회
    @Query("SELECT ii FROM ItemInventory ii WHERE ii.item.itemSeq = :itemSeq " +
            "AND ii.itemInventoryExpirationDate >= CURRENT_DATE " +
            "ORDER BY ii.itemInventoryExpirationDate ASC")
    List<ItemInventory> findValidInventoriesByItemSeq(Long itemSeq);
}
