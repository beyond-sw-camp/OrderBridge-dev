package error.pirate.backend.workOrder.query.mapper;


import error.pirate.backend.workOrder.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WorkOrderMapper {

    /* 작업지시서 목록 조회 */
    List<WorkOrderListDTO> readWorkOrderList(@Param("filter") WorkOrderFilterDTO filter,
                                             @Param("offset") int offset);

    /* 작업지시서 목록 개수 조회 */
    long readWorkOrderListCount(@Param("filter") WorkOrderFilterDTO filter);

    /* 작업지시서 상세 조회 */
    WorkOrderDetailDTO readWorkOrder(Long workOrderSeq);

    /* 작업지시서 상세품목 조회 */
    WorkOrderItemDTO readItemByWorkOrderSeq(Long workOrderSeq);

    /* 작업지시서 현황 조회 */
    List<WorkOrderSituationDTO> readWorkOrderSituations(LocalDate startDate, LocalDate endDate, String clientName, String wareHouseName);

}
