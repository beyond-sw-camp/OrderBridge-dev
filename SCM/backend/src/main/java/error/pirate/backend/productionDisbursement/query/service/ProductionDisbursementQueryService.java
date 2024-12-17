package error.pirate.backend.productionDisbursement.query.service;

import error.pirate.backend.productionDisbursement.query.mapper.ProductionDisbursementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionDisbursementQueryService {

    private final ProductionDisbursementMapper productionDisbursementMapper;

}
