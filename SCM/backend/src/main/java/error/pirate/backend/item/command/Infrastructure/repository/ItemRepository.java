package error.pirate.backend.item.command.Infrastructure.repository;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
