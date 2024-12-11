package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "출하전표 현황 응답")
public class ShippingSlipSituationResponse {
    private List<ShippingSlipSituationDTO> shippingSlipSituationList;
}
