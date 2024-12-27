package error.pirate.backend.productionDisbursement.query.mapper;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.productionDisbursement.query.dto.ProductionDisbursementDetailDTO;
import error.pirate.backend.productionDisbursement.query.dto.ProductionDisbursementItemDTO;
import error.pirate.backend.productionDisbursement.query.dto.ProductionDisbursementListDTO;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ProductionDisbursementMapper {
    // 생산불출 목록 조회
    List<ProductionDisbursementListDTO> readProductionDisbursementList(
            @Param("factoryName") String factoryName,
            @Param("statusList") List<ProductionDisbursementStatus> statusList,
            @Param("startDate") LocalDateTime startDateTime,
            @Param("endDate") LocalDateTime endDateTime,
            @Param("offset") int offset,
            @Param("size") Integer size);

    // 생산불출 목록 개수 조회
    long countProductionDisbursementList(@Param("factoryName") String factoryName,
                                         @Param("statusList") List<ProductionDisbursementStatus> statusList,
                                         @Param("startDate") LocalDateTime startDateTime,
                                         @Param("endDate") LocalDateTime endDateTime
    );

    // 생산불출 상세조회
    ProductionDisbursementDetailDTO readProductionDisbursement(@Param("productionDisbursementSeq")Long productionDisbursementSeq);

    // 생산불출 상세품목조회
    List<ProductionDisbursementItemDTO> readProductionDisbursementItem(@Param("productionDisbursementSeq")Long productionDisbursementSeq);

    // 생산불출 엑셀 다운로드
    ArrayList<Object> downloadProductionDisbursementListExcel(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("factoryName")  String factoryName,
            @Param("productionDisbursementStatus") List<ProductionDisbursementStatus> productionDisbursementStatus);


}
