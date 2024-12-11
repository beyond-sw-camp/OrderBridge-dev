package error.pirate.backend.item.command.domain.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemUnitRepository extends JpaRepository<ItemUnit,Long> {
}
