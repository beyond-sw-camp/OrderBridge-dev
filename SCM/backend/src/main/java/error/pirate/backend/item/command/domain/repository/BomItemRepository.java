package error.pirate.backend.item.command.domain.repository;


import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BomItemRepository extends JpaRepository<BomItem, Long> {
    void deleteAllByParentItem(Item item);

    List<BomItem> findAllByParentItem(Item parentItem);
}
