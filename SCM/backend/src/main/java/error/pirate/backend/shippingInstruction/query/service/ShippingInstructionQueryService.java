package error.pirate.backend.shippingInstruction.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.common.RemainingQuantity;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.query.dto.*;
import error.pirate.backend.shippingInstruction.query.mapper.ShippingInstructionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingInstructionQueryService {

    private final ShippingInstructionMapper shippingInstructionMapper;
    private final ExcelDownLoad excelDownBody;
    private final RemainingQuantity remainingQuantity;

    /* 출하지시서 리스트 조회 */
    @Transactional(readOnly = true)
    public ShippingInstructionListResponse readShippingInstructionList(ShippingInstructionListRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        if(request.getEndDate() != null){
            request.setEndDate(request.getEndDate().plusDays(1));   // 하루 추가
        }
        List<ShippingInstructionStatus> statusList = request.getShippingInstructionStatus();   // 상태 리스트

        // 리스트 응답 및 상태를 value로 변경
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

    /* 출하지시서 엑셀 다운 */
    public byte[] shippingInstructionExcelDown(ShippingInstructionListRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        request.setSize(null);
        List<ShippingInstructionStatus> statusList = request.getShippingInstructionStatus();   // 상태 리스트

        List<ShippingInstructionListDTO> shippingInstructionList
                = shippingInstructionMapper.selectShippingInstructionList(offset, request, statusList);

        String[] headers = {"출하지시서명", "출하지시서 품목", "거래처명", "출하예정일", "상태"};
        String[][] excel = new String[shippingInstructionList.size()][headers.length];

        for (int i = 0; i < shippingInstructionList.size(); i++) {
            ShippingInstructionListDTO dto = shippingInstructionList.get(i);

            excel[i][0] = dto.getShippingInstructionName();
            excel[i][1] = dto.getItemName();//  품목
            excel[i][2] = dto.getClientName();
            excel[i][3] = dto.getShippingInstructionScheduledShipmentDate() != null
                    ? dto.getShippingInstructionScheduledShipmentDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                    : null;
            excel[i][4] = ShippingInstructionStatus.statusValue(
                    String.valueOf(dto.getShippingInstructionStatus()));
        }

        return excelDownBody.excelDownBody(excel, headers, "출하지시서");
    }

    /* 출하지시서 현황 조회 */
    @Transactional(readOnly = true)
    public List<ShippingInstructionSituationResponse> readShippingInstructionSituation(ShippingInstructionSituationRequest request) {
        return shippingInstructionMapper.selectShippingInstructionSituation(request);
    }

    /* 출하지시서 현황 엑셀 다운 */
    public byte[] shippingInstructionSituationExcelDown(ShippingInstructionSituationRequest request) {

        List<ShippingInstructionSituationResponse> shippingInstructionSituationList
                = shippingInstructionMapper.selectShippingInstructionSituation(request);

        String[] headers = {"번호", "출하예정일", "출하지시서명", "총수량", "거래처명", "출하주소", "출하지시서 비고"};
        String[][] excel = new String[shippingInstructionSituationList.size()][headers.length];

        for(int i = 0; i < shippingInstructionSituationList.size(); i++) {
            ShippingInstructionSituationResponse dto = shippingInstructionSituationList.get(i);
            if(dto.getShippingInstructionScheduledShipmentDate() == null) {
                boolean isNull = (dto.getShippingInstructionScheduledShipmentMonthDate() == null);

                excel[i][0] = "-";
                excel[i][1] = isNull ? "" : dto.getShippingInstructionScheduledShipmentMonthDate(); // 출하예정월
                excel[i][2] = isNull ? "총합" : "-";
                excel[i][3] = dto.getShippingInstructionTotalQuantitySum() + " 개";  // 총수량
                excel[i][4] = "-";
                excel[i][5] = "-";
                excel[i][6] = "-";
            } else {
                excel[i][0] = String.valueOf(i+1); // 번호
                excel[i][1] = dto.getShippingInstructionScheduledShipmentDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")); // 출하예정일
                excel[i][2] = dto.getShippingInstructionName();       // 출하지시서명
                excel[i][3] = dto.getShippingInstructionTotalQuantity() + " 개";  // 총수량
                excel[i][4] = dto.getClientName(); // 거래처명
                excel[i][5] = dto.getShippingInstructionAddress(); // 주소
                excel[i][6] = dto.getShippingInstructionNote(); // 비고
            }
        }

        return excelDownBody.excelDownBody(excel, headers, "출하지시서 현황");
    }

    // 출하지시서 품목 값 확인
    public List<ShippingInstructionItemCheckDTO> shippingInstructionItemCheck(long salesOrderSeq) {
        return shippingInstructionMapper.sumShippingInstructionItemValue(salesOrderSeq);
    }

    // 출하지시서 등록 시 남아있는 주문서 품목 값 리턴
    public List<Integer> readRemainingQuantity(long salesOrderSeq) {
        return remainingQuantity.remainingQuantity(SalesOrder.class, ShippingInstruction.class, salesOrderSeq);
    }
}
