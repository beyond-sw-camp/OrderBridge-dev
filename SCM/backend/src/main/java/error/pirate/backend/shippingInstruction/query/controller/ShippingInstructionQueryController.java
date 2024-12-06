package error.pirate.backend.shippingInstruction.query.controller;

import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionListResponse;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionResponse;
import error.pirate.backend.shippingInstruction.query.service.ShippingInstructionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "출하지시서 컨트롤러", description = "출하지시서 조회")
@RestController(value = "shippingInstructionController")
@RequiredArgsConstructor    // final 을 받은 필드의 생성자를 주입
@RequestMapping("/api/v1")
@Slf4j
public class ShippingInstructionQueryController {
    private final ShippingInstructionQueryService shippingInstructionQueryService;

    @Operation(summary = "출하지시서 조회", description = "출하지시서 조회")
    @GetMapping("/shipping-instruction")
    public ResponseEntity<ShippingInstructionListResponse> readShippingInstructionList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String shippingInstructionStatus
    ) {
        ShippingInstructionListResponse response
                = shippingInstructionQueryService.readShippingInstructionList(page, size, startDate, endDate, clientName, shippingInstructionStatus);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "출하지시서 상세조회", description = "출하지시서 상세조회")
    @GetMapping("/shipping-instruction/{shippingInstructionSeq}")
    public ResponseEntity<ShippingInstructionResponse> readShippingInstruction(
            @PathVariable long shippingInstructionSeq
    ) {
        ShippingInstructionResponse response
                = shippingInstructionQueryService.readShippingInstruction(shippingInstructionSeq);

        return ResponseEntity.ok(response);
    }


}
