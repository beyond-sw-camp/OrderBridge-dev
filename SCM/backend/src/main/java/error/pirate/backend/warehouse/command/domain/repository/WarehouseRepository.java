package error.pirate.backend.warehouse.command.domain.repository;

import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}