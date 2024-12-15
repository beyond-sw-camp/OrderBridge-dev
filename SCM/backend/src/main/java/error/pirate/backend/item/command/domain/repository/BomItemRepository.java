package error.pirate.backend.item.command.domain.repository;


import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BomItemRepository extends JpaRepository<BomItem, Long> {
    void deleteAllByParentItem(Item item);

    List<BomItem> findAllByParentItem(Item parentItem);

}
