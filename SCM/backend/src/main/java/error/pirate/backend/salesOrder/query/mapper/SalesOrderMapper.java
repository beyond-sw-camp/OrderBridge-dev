package error.pirate.backend.salesOrder.query.mapper;

import error.pirate.backend.salesOrder.query.dto.SalesOrderListItemDTO;
import error.pirate.backend.salesOrder.query.dto.SalesOrderSituationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SalesOrderMapper {
    // 주문서 목록 조회
    public List<SalesOrderListItemDTO> selectSalesOrderList(
            @Param("offset") int i,
            @Param("limit") Integer limit,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName,
            @Param("salesOrderStatus") String salesOrderStatus);

    // 주문서 목록 개수 조회
    int countSalesOrderList(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName,
            @Param("salesOrderStatus") String salesOrderStatus);

    // 주문서 현황 조회
    List<SalesOrderSituationDTO> selectSalesOrderSituation(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName);
}
