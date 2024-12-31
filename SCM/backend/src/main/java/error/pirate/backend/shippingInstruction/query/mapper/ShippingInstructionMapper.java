package error.pirate.backend.shippingInstruction.query.mapper;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import error.pirate.backend.shippingInstruction.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ShippingInstructionMapper {
    // 출하지시서 목록 조회
    List<ShippingInstructionListDTO> selectShippingInstructionList(
            @Param("offset") int offset,
            @Param("request") ShippingInstructionListRequest request,
            @Param("statusList") List<ShippingInstructionStatus> statusList);

    // 출하지시서 목록 갯수
    long countShippingInstruction(
            @Param("request") ShippingInstructionListRequest request,
            @Param("statusList") List<ShippingInstructionStatus> statusList);

    // 출하지시서 상세 조회
    ShippingInstructionDTO selectShippingInstructionByShippingInstructionSeq(long shippingInstructionSeq);

    // 출하지시서 상세 품목 조회
    List<ShippingInstructionItemDTO> selectItemListByShippingInstructionSeq(long shippingInstructionSeq);

    // 출하지시서 현황 조회
    List<ShippingInstructionSituationResponse> selectShippingInstructionSituation(
            @Param("request") ShippingInstructionSituationRequest request);

    // 출하지시서 품목 총합계 조회
    List<ShippingInstructionItemCheckDTO> sumShippingInstructionItemValue(long salesOrderSeq);

    // 출하지시서 목록 엑셀 다운로드
    ArrayList<Object> selectShippingInstructionExcel(
            @Param("request") ShippingInstructionListRequest request,
            @Param("statusList") List<ShippingInstructionStatus> statusList);
}
