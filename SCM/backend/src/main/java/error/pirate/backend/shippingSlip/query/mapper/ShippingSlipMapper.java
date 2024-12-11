package error.pirate.backend.shippingSlip.query.mapper;

import error.pirate.backend.shippingSlip.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingSlipMapper {
    List<ShippingSlipListDTO> selectShippingSlipList(
            @Param("offset") int offset,
            @Param("request") ShippingSlipListRequest request);

    long countShippingSlip(
            @Param("request") ShippingSlipListRequest request);

    ShippingSlipDTO selectShippingSlipByShippingSlipSeq(long shippingSlipSeq);

    List<ShippingSlipItemDTO> selectItemListByShippingSlipSeq(long shippingSlipSeq);

    List<ShippingSlipSituationDTO> selectShippingSlipSituation(
            @Param("request") ShippingSlipSituationRequest request);
}
