package error.pirate.backend.shippingSlip.query.service;

import error.pirate.backend.shippingSlip.query.dto.*;
import error.pirate.backend.shippingSlip.query.mapper.ShippingSlipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingSlipQueryService {

    private final ShippingSlipMapper shippingSlipMapper;

    /* 출하전표 리스트 조회 */
    @Transactional(readOnly = true)
    public ShippingSlipListResponse readShippingSlipList(ShippingSlipListRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        List<ShippingSlipListDTO> shippingSlipList
                = shippingSlipMapper.selectShippingSlipList(offset, request);

        long totalItems = shippingSlipMapper.countShippingSlip(request);

        return ShippingSlipListResponse.builder()
                .shippingSlipList(shippingSlipList)
                .currentPage(request.getPage())
                .totalPages((int) Math.ceil((double) totalItems / request.getSize()))
                .totalItems(totalItems)
                .build();
    }

    /* 출하전표 상세 조회 */
    @Transactional(readOnly = true)
    public ShippingSlipResponse readShippingSlip(long shippingSlipSeq) {
        // 품목을 제외한 출하전표 조회
        ShippingSlipDTO shippingSlip = shippingSlipMapper.selectShippingSlipByShippingSlipSeq(shippingSlipSeq);

        // 출하전표에 해당하는 품목 리스트 조회
        List<ShippingSlipItemDTO> itemList
                = shippingSlipMapper.selectItemListByShippingSlipSeq(shippingSlipSeq);

        return ShippingSlipResponse.builder()
                .shippingSlipDTO(shippingSlip)
                .itemList(itemList)
                .build();
    }

    /* 출하전표 현황 조회 */
    @Transactional(readOnly = true)
    public ShippingSlipSituationResponse readShippingSlipSituation(ShippingSlipSituationRequest request) {
        List<ShippingSlipSituationDTO> shippingSlipSituationList
                = shippingSlipMapper.selectShippingSlipSituation(request);

        return ShippingSlipSituationResponse.builder()
                .shippingSlipSituationList(shippingSlipSituationList)
                .build();
    }
}
