package error.pirate.backend.purchase.command.application.controller;

import error.pirate.backend.purchase.command.application.dto.PurchaseCreateRequest;
import error.pirate.backend.purchase.command.application.service.PurchaseApplicationService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchase")
@Tag(name = "구매 API", description = "구매 API")
public class PurchaseController {

    private final PurchaseApplicationService purchaseApplicationService;

    @PostMapping
    @Operation(summary = "구매서 등록")
    public ResponseEntity<Void> createPurchase(@RequestBody PurchaseCreateRequest request) {
        purchaseApplicationService.createPurchase(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
