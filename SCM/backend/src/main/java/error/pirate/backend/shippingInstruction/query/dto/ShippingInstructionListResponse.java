package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "출하지시서 리스트 응답")
public class ShippingInstructionListResponse {
    private List<ShippingInstructionListDTO> shippingInstructionList;
    private int currentPage;            // 현재 페이지
    private int totalPages;             // 전체 페이지 수
    private long totalItems;            // 총 아이템 수
}
