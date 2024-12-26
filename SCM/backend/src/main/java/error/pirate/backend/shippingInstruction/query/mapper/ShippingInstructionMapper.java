package error.pirate.backend.shippingInstruction.query.mapper;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import error.pirate.backend.shippingInstruction.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingInstructionMapper {
    List<ShippingInstructionListDTO> selectShippingInstructionList(
            @Param("offset") int offset,
            @Param("request") ShippingInstructionListRequest request,
            @Param("statusList") List<ShippingInstructionStatus> statusList);

    long countShippingInstruction(
            @Param("request") ShippingInstructionListRequest request,
            @Param("statusList") List<ShippingInstructionStatus> statusList);

    ShippingInstructionDTO selectShippingInstructionByShippingInstructionSeq(long shippingInstructionSeq);

    List<ShippingInstructionItemDTO> selectItemListByShippingInstructionSeq(long shippingInstructionSeq);

    List<ShippingInstructionSituationResponse> selectShippingInstructionSituation(
            @Param("request") ShippingInstructionSituationRequest request);

    List<ShippingInstructionItemCheckDTO> sumShippingInstructionItemValue(long salesOrderSeq);
}
