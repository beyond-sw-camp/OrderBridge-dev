package error.pirate.backend.shippingInstruction.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.common.RemainingQuantity;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
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

    /* 출하지시서 현황 조회 */
    @Transactional(readOnly = true)
    public List<ShippingInstructionSituationResponse> readShippingInstructionSituation(ShippingInstructionSituationRequest request) {
        return shippingInstructionMapper.selectShippingInstructionSituation(request);
    }

    /* 출하지시서 엑셀 다운 */
    @Transactional(readOnly = true)
    public byte[] shippingInstructionExcel(ShippingInstructionListRequest request) {
        List<ShippingInstructionStatus> statusList = request.getShippingInstructionStatus();   // 상태 리스트

        return excelDownBody.writeCells(
                new String[] {"등록일", "수정일", "이름", "상태", "출하예정일", "총 수량", "출하주소", "비고"},
                shippingInstructionMapper.selectShippingInstructionExcel(request, statusList)
        );
    }

    // 출하지시서 품목 값 확인
    @Transactional(readOnly = true)
    public List<ShippingInstructionItemCheckDTO> shippingInstructionItemCheck(long salesOrderSeq) {
        return shippingInstructionMapper.sumShippingInstructionItemValue(salesOrderSeq);
    }

    // 출하지시서 등록 시 남아있는 주문서 품목 값 리턴
    @Transactional(readOnly = true)
    public List<Integer> readRemainingQuantity(long salesOrderSeq) {
        return remainingQuantity.remainingQuantity(SalesOrder.class, ShippingInstruction.class, salesOrderSeq);
    }
}
