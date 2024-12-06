package error.pirate.backend.workOrder.query.mapper;


import error.pirate.backend.workOrder.query.dto.WorkOrderListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WorkOrderMapper {

    List<WorkOrderListDTO> readWorkOrderList(@Param("warehouseName") String warehouseName,
                                             @Param("workOrderStatus") String workOrderStatus,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,
                                             @Param("page") Integer page,
                                             @Param("size") Integer size);

    long readWorkOrderListCount(@Param("warehouseName") String warehouseName,
                                @Param("workOrderStatus") String workOrderStatus,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);
}
