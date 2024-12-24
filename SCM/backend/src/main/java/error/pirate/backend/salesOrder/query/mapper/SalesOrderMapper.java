package error.pirate.backend.salesOrder.query.mapper;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.query.dto.*;
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
            @Param("salesOrderStatus") List<SalesOrderStatus> salesOrderStatus);

    // 주문서 목록 개수 조회
    int countSalesOrderList(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName,
            @Param("salesOrderStatus") List<SalesOrderStatus> salesOrderStatus);

    // 주문서 상세 조회
    SalesOrderDTO selectSalesOrder(
            @Param("salesOrderSeq") Long salesOrderSeq);

    // 주문서 상세 품목 조회
    List<SalesOrderItemDTO> selectSalesOrderItem(
            @Param("salesOrderSeq") Long salesOrderSeq);

    // 주문서 현황 조회
    List<SalesOrderSituationDTO> selectSalesOrderSituation(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName);

    // 주문서 품목 값 확인
    List<SalesOrderItemCheckDTO> sumSalesOrderItemValue(
            @Param("quotationSeq") Long quotationSeq);
}
