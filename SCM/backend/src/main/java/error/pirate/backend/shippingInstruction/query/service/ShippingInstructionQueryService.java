package error.pirate.backend.shippingInstruction.query.service;

import error.pirate.backend.shippingInstruction.query.dto.*;
import error.pirate.backend.shippingInstruction.query.mapper.ShippingInstructionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingInstructionQueryService {

    private final ShippingInstructionMapper shippingInstructionMapper;

    /* 출하지시서 리스트 조회 */
    @Transactional(readOnly = true)
    public ShippingInstructionListResponse readShippingInstructionList(ShippingInstructionListRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        List<String> statusList = request.getShippingInstructionStatus();   // 상태 리스트

        List<ShippingInstructionListDTO> shippingInstructionList
                = shippingInstructionMapper.selectShippingInstructionList(offset, request, statusList);

        long totalItems = shippingInstructionMapper.countShippingInstruction(request, statusList);

        return ShippingInstructionListResponse.builder()
                .shippingInstructionList(shippingInstructionList)
                .currentPage(request.getPage())
                .totalPages((int) Math.ceil((double) totalItems / request.getSize()))
                .totalItems(totalItems)
                .build();
    }

    /* 출하지시서 상세 조회 */
    @Transactional(readOnly = true)
    public ShippingInstructionResponse readShippingInstruction(long shippingInstructionSeq) {

        // 품목을 제외한 출하지시서 조회
        ShippingInstructionDTO shippingInstruction = shippingInstructionMapper.selectShippingInstructionByShippingInstructionSeq(shippingInstructionSeq);

        // 출하지시서에 해당하는 품목 리스트 조회
        List<ShippingInstructionItemDTO> itemList
                = shippingInstructionMapper.selectItemListByShippingInstructionSeq(shippingInstructionSeq);

        return ShippingInstructionResponse.builder()
                .shippingInstructionDTO(shippingInstruction)
                .itemList(itemList)
                .build();
    }

    /* 출하지시서 현황 조회 */
    @Transactional(readOnly = true)
    public ShippingInstructionSituationResponse readShippingInstructionSituation(ShippingInstructionSituationRequest request) {
        // 출하지시서 현황 조회
        List<ShippingInstructionSituationDTO> shippingInstructionSituationList
                = shippingInstructionMapper.selectShippingInstructionSituation(request);

        // 출하지시서 현황 월합계
        List<ShippingInstructionSituationMonthlyTotalDTO> monthlyTotalList
                = shippingInstructionMapper.selectMonthlyTotal(request);

        // 출하전표 현황 총합계
        int totalQuantity = shippingInstructionMapper.selectTotal(request);

        return ShippingInstructionSituationResponse.builder()
                .shippingInstructionSituationList(shippingInstructionSituationList)
                .monthlyTotalList(monthlyTotalList)
                .totalQuantity(totalQuantity)
                .build();
    }
}
