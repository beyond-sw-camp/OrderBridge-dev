package error.pirate.backend.item.command.domain.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // 활성화된 품목만 조회
    List<Item> findAllByItemStatus(ItemStatus itemStatus);
}
