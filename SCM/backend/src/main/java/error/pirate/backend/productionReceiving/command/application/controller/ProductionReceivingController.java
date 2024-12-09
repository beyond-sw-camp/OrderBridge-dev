package error.pirate.backend.productionReceiving.command.application.controller;

import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.application.service.ProductionReceivingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/productionReceiving")
@Tag(name = "생산입고 API", description = "생산입고 API")
public class ProductionReceivingController {

    private final ProductionReceivingService productionReceivingService;

    @PostMapping
    @Operation(summary = "생산입고 등록")
    public ResponseEntity<Void> createProductionReceiving(@RequestBody ProductionReceivingCreateRequest request) {
        productionReceivingService.createProductionReceiving(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
