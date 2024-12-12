package error.pirate.backend.salesOrder.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesOrderItemRepository  extends JpaRepository<SalesOrderItem, Long> {

    // 특정 주문서(salesOrderSeq)와 연결된 품목 조회
    @Query("SELECT si FROM SalesOrderItem si WHERE si.salesOrder.salesOrderSeq = :salesOrderSeq")
    List<SalesOrderItem> findAllBySalesOrderSeq(@Param("salesOrderSeq") Long salesOrderSeq);
}
