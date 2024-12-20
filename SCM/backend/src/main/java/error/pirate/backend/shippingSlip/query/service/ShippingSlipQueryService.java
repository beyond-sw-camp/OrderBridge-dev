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

    /* 출하전표 엑셀 다운 */
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
                    ? dto.getShippingSlipShippingDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                    : null;
        }

        return excelDownBody.excelDownBody(excel, headers, "출하전표");
    }

    /* 출하전표 현황 조회 */
    @Transactional(readOnly = true)
    public List<ShippingSlipSituationResponse> readShippingSlipSituation(ShippingSlipSituationRequest request) {
        return shippingSlipMapper.selectShippingSlipSituation(request);
    }

    /* 출하전표 현황 엑셀 다운 */
    public byte[] shippingSlipSituationExcelDown(ShippingSlipSituationRequest request) {

        List<ShippingSlipSituationResponse> shippingSlipSituationList
                = shippingSlipMapper.selectShippingSlipSituation(request);

        String[] headers = {"번호", "출하일", "출하전표명", "총수량", "거래처명", "출하주소", "출하전표 비고"};
        String[][] excel = new String[shippingSlipSituationList.size()][headers.length];

        for(int i = 0; i < shippingSlipSituationList.size(); i++) {
            ShippingSlipSituationResponse dto = shippingSlipSituationList.get(i);
            if(dto.getShippingSlipShippingDate() == null) {
                boolean isNull = (dto.getShippingSlipShippingMonthDate() == null);

                excel[i][0] = "-";
                excel[i][1] = isNull ? "" : dto.getShippingSlipShippingMonthDate(); // 출하 월
                excel[i][2] = isNull ? "총합" : "-";
                excel[i][3] = dto.getShippingSlipTotalQuantitySum() + " 개";  // 총수량
                excel[i][4] = "-";
                excel[i][5] = "-";
                excel[i][6] = "-";
            } else {
                excel[i][0] = String.valueOf(i+1); // 번호
                excel[i][1] = dto.getShippingSlipShippingDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")); // 출하예정일
                excel[i][2] = dto.getShippingSlipName();       // 출하지시서명
                excel[i][3] = dto.getShippingSlipTotalQuantity() + " 개";  // 총수량
                excel[i][4] = dto.getClientName(); // 거래처명
                excel[i][5] = dto.getShippingAddress().getValue(); // 주소
                excel[i][6] = dto.getShippingSlipNote(); // 비고
            }
        }

        return excelDownBody.excelDownBody(excel, headers, "출하전표 현황");
    }
}
