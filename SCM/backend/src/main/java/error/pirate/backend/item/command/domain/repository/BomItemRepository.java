package error.pirate.backend.item.command.domain.repository;


import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BomItemRepository extends JpaRepository<BomItem, Long> {
    void deleteAllByParentItem(Item item);

    List<BomItem> findAllByParentItem(Item parentItem);

    @Query("SELECT b FROM BomItem b WHERE b.childItem.itemSeq = :itemSeq AND b.parentItem.itemSeq = :parentItemSeq")
    Optional<BomItem> findByChildItem_ItemSeq(@Param("itemSeq") Long itemSeq,
                                          @Param("parentItemSeq") Long parentItemSeq);
}
