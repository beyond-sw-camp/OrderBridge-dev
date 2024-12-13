package error.pirate.backend.productionReceiving.query.mapper;

import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingSituationRequest;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingSituationResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductionReceivingMapper {

    List<ProductionReceivingSituationResponse> findProductionReceivingSituationByFilter(ProductionReceivingSituationRequest request);
}
