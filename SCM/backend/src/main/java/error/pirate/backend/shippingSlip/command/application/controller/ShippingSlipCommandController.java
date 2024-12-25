package error.pirate.backend.shippingSlip.command.application.controller;

import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.application.service.ShippingSlipApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "출하전표", description = "출하전표 조회/등록/수정/삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/shipping-slip")
@Slf4j
public class ShippingSlipCommandController {

    private final ShippingSlipApplicationService shippingSlipApplicationService;

    // 출하전표 작성
    @Operation(summary = "출하전표 작성", description = "출하전표를 작성한다.")
    @PostMapping
    public ResponseEntity<String> createShippingSlip(
            @RequestBody ShippingSlipRequest shippingSlipRequest
    ) {

        shippingSlipApplicationService.createShippingSlip(shippingSlipRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("출하전표 작성성공");
    }
}
