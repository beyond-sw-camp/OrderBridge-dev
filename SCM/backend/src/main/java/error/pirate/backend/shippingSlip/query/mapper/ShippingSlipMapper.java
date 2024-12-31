package error.pirate.backend.shippingSlip.query.mapper;

import error.pirate.backend.shippingSlip.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ShippingSlipMapper {
    // 출하전표 목록 조회
    List<ShippingSlipListDTO> selectShippingSlipList(
            @Param("offset") int offset,
            @Param("request") ShippingSlipListRequest request);

    // 출하전표 목록 갯수
    long countShippingSlip(
            @Param("request") ShippingSlipListRequest request);

    // 출하전표 상세 조회
    ShippingSlipDTO selectShippingSlipByShippingSlipSeq(long shippingSlipSeq);

    // 출하전표 상세 품목 조회
    List<ShippingSlipItemDTO> selectItemListByShippingSlipSeq(long shippingSlipSeq);

    // 출하전표 현황 조회
    List<ShippingSlipSituationResponse> selectShippingSlipSituation(
            @Param("request") ShippingSlipSituationRequest request);

    // 출하전표 목록 엑셀 다운로드
    ArrayList<Object> selectShippingSlipExcel(
            @Param("request") ShippingSlipListRequest request);
}
