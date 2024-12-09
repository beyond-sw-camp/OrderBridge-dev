package error.pirate.backend.productionReceiving.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionReceivingListResponse {
    private Page<ProductionReceivingListDTO> productionReceivingList;
}
