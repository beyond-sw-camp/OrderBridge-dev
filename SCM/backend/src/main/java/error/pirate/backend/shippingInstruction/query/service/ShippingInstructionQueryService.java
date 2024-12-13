package error.pirate.backend.shippingInstruction.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponse;
import error.pirate.backend.shippingInstruction.query.dto.*;
import error.pirate.backend.shippingInstruction.query.mapper.ShippingInstructionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingInstructionQueryService {

    private final ShippingInstructionMapper shippingInstructionMapper;
    private final ExcelDownLoad excelDownBody;

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

    /* 출하지시서 엑셀 다운 */
    public byte[] shippingInstructionExcelDown(ShippingInstructionListRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        request.setSize(null);
        List<String> statusList = request.getShippingInstructionStatus();   // 상태 리스트

        List<ShippingInstructionListDTO> shippingInstructionList
                = shippingInstructionMapper.selectShippingInstructionList(offset, request, statusList);

        String[] headers = {"출하지시서명", "출하지시서 품목", "거래처명", "출하예정일", "상태"};
        String[][] excel = new String[shippingInstructionList.size()][headers.length];

        for(int i=0 ; i<shippingInstructionList.size() ; i++) {
            ShippingInstructionListDTO dto = shippingInstructionList.get(i);

            excel[i][0] = dto.getShippingInstructionName();
            excel[i][1] = dto.getItemName();//  품목
            excel[i][2] = dto.getClientName();
            excel[i][3] = dto.getShippingInstructionScheduledShipmentDate() != null
                    ? dto.getShippingInstructionScheduledShipmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : null;
            excel[i][4] = String.valueOf(dto.getShippingInstructionStatus());
        }

        return excelDownBody.excelDownBody(excel, headers, "출하지시서");
    }
}
