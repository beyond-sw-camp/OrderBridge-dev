package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "출하전표 상세 응답")
public class ShippingSlipResponse {
    private ShippingSlipDTO shippingSlipDTO;
    private List<ShippingSlipItemDTO> itemList;
}
