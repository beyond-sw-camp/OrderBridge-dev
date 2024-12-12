package error.pirate.backend.shippingInstruction.query.mapper;

import error.pirate.backend.shippingInstruction.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingInstructionMapper {
    List<ShippingInstructionListDTO> selectShippingInstructionList(
            @Param("offset") int offset,
            @Param("request") ShippingInstructionListRequest request,
            @Param("statusList") List<String> statusList);

    long countShippingInstruction(
            @Param("request") ShippingInstructionListRequest request,
            @Param("statusList") List<String> statusList);

    ShippingInstructionDTO selectShippingInstructionByShippingInstructionSeq(long shippingInstructionSeq);

    List<ShippingInstructionItemDTO> selectItemListByShippingInstructionSeq(long shippingInstructionSeq);

    List<ShippingInstructionSituationDTO> selectShippingInstructionSituation(
            @Param("request") ShippingInstructionSituationRequest request);

    List<ShippingInstructionSituationMonthlyTotalDTO> selectMonthlyTotal(
            @Param("request") ShippingInstructionSituationRequest request);

    int selectTotal(@Param("request") ShippingInstructionSituationRequest request);
}
