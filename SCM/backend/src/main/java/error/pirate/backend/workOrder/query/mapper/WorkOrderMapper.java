package error.pirate.backend.workOrder.query.mapper;


import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import error.pirate.backend.workOrder.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface WorkOrderMapper {

    /* 작업지시서 목록 조회 */
    List<WorkOrderListDTO> readWorkOrderList(@Param("startDateTime") LocalDateTime startDateTime,
                                             @Param("endDateTime") LocalDateTime endDateTime,
                                             @Param("statusList") List<WorkOrderStatus> statusList,
                                             @Param("offset") int offset,
                                             @Param("filter") WorkOrderFilterDTO filter);

    /* 작업지시서 목록 개수 조회 */
    long readWorkOrderListCount(@Param("startDateTime") LocalDateTime startDateTime,
                                @Param("endDateTime") LocalDateTime endDateTime,
                                @Param("statusList") List<WorkOrderStatus> statusList,
                                @Param("filter") WorkOrderFilterDTO filter);

    /* 작업지시서 상세 조회 */
    WorkOrderDetailDTO readWorkOrder(@Param("workOrderSeq") Long workOrderSeq);

    /* 작업지시서 상세품목 조회 */
    WorkOrderItemDTO readItemByWorkOrderSeq(@Param("workOrderSeq") Long workOrderSeq);

    /* 작업지시서 현황 조회 */
    List<WorkOrderSituationDTO> readWorkOrderSituations(
            @Param("startDate") LocalDateTime startDateTime,
            @Param("endDate") LocalDateTime EndDateTime,
            @Param("clientName") String clientName,
            @Param("warehouseName") String warehouseName);

    /* 작업지시서 목록 엑셀 다운로드 */
    ArrayList<Object> readWorkOrderExcel(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseName")  String warehouseName,
            @Param("workOrderStatus") List<WorkOrderStatus> workOrderStatus);

    /* 작업지시서 현황 엑셀 다운로드 */
    ArrayList<Object> readWorkOrderSituationExcel(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseName") String warehouseName,
            @Param("clientName") String clientName);

}
