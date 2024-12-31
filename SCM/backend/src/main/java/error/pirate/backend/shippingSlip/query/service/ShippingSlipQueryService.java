package error.pirate.backend.shippingSlip.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionListDTO;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionSituationResponse;
import error.pirate.backend.shippingSlip.query.dto.*;
import error.pirate.backend.shippingSlip.query.mapper.ShippingSlipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingSlipQueryService {

    private final ShippingSlipMapper shippingSlipMapper;
    private final ExcelDownLoad excelDownBody;

    /* 출하전표 리스트 조회 */
    @Transactional(readOnly = true)
    public ShippingSlipListResponse readShippingSlipList(ShippingSlipListRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        if(request.getEndDate() != null){
            request.setEndDate(request.getEndDate().plusDays(1));   // 하루 추가
        }
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
    public List<ShippingSlipSituationResponse> readShippingSlipSituation(ShippingSlipSituationRequest request) {
        return shippingSlipMapper.selectShippingSlipSituation(request);
    }

    /* 출하전표 엑셀 다운 */
    @Transactional(readOnly = true)
    public byte[] shippingSlipExcel(ShippingSlipListRequest request) {
        return excelDownBody.writeCells(
                new String[] {"등록일", "수정일", "이름", "출하일", "총 수량", "출하주소", "비고"},
                shippingSlipMapper.selectShippingSlipExcel(request)
        );
    }
}
