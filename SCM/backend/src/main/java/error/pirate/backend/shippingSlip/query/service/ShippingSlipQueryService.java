package error.pirate.backend.shippingSlip.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionListDTO;
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
        // 출하전표 현황 조회
        List<ShippingSlipSituationDTO> shippingSlipSituationList
                = shippingSlipMapper.selectShippingSlipSituation(request);

        // 출하전표 현황 월합계
        List<ShippingSlipSituationMonthlyTotalDTO> monthlyTotalList
                = shippingSlipMapper.selectMonthlyTotal(request);

        // 출하전표 현황 총합계
        int totalQuantity = shippingSlipMapper.selectTotal(request);

        return ShippingSlipSituationResponse.builder()
                .shippingSlipSituationList(shippingSlipSituationList)
                .monthlyTotalList(monthlyTotalList)
                .totalQuantity(totalQuantity)
                .build();
    }

    public byte[] shippingSlipExcelDown(ShippingSlipListRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        request.setSize(null);
        List<ShippingSlipListDTO> shippingSlipList
                = shippingSlipMapper.selectShippingSlipList(offset, request);

        String[] headers = {"출하전표명", "출하전표 품목", "거래처명", "출하일"};
        String[][] excel = new String[shippingSlipList.size()][headers.length];

        for(int i = 0; i< shippingSlipList.size() ; i++) {
            ShippingSlipListDTO dto = shippingSlipList.get(i);

            excel[i][0] = dto.getShippingSlipName();
            excel[i][1] = dto.getItemName();//  품목
            excel[i][2] = dto.getClientName();
            excel[i][3] = dto.getShippingSlipShippingDate() != null
                    ? dto.getShippingSlipShippingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : null;
        }

        return excelDownBody.excelDownBody(excel, headers, "출하지시서");
    }
}
