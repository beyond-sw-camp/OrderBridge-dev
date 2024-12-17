package error.pirate.backend.productionDisbursement.query.controller;

import error.pirate.backend.productionDisbursement.query.service.ProductionDisbursementQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productionDisbursement")
@Slf4j
@Tag(name = "Production Disbursement", description = "생산불출")
public class ProductionDisbursementQueryController {

    private final ProductionDisbursementQueryService productionDisbursementQueryService;


}
