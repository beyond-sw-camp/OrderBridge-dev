package error.pirate.backend.workOrder.command.domain.repository;


import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
}
