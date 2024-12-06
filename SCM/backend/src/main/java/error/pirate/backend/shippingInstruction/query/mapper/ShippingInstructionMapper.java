package error.pirate.backend.shippingInstruction.query.mapper;

import error.pirate.backend.shippingInstruction.query.dto.ItemDTO;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionListDTO;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ShippingInstructionMapper {
    List<ShippingInstructionListDTO> selectShippingInstructionList(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName,
            @Param("shippingInstructionStatus") String shippingInstructionStatus);

    long countShippingInstruction(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientName") String clientName,
            @Param("shippingInstructionStatus") String shippingInstructionStatus);

    ShippingInstructionResponse selectShippingInstructionByShippingInstructionSeq(long shippingInstructionSeq);

    List<ItemDTO> selectItemListByShippingInstructionSeq(long shippingInstructionSeq);
}
