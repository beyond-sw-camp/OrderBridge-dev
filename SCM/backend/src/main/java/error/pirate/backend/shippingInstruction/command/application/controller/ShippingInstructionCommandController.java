package error.pirate.backend.shippingInstruction.command.application.controller;

import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import error.pirate.backend.shippingInstruction.command.application.service.ShippingInstructionApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "출하지시서", description = "출하지시서 조회/등록/수정/삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/shipping-instruction")
@Slf4j
public class ShippingInstructionCommandController {

    private final ShippingInstructionApplicationService shippingInstructionApplicationService;

    // 출하지시서 작성
    @Operation(summary = "출하지시서 작성", description = "출하지시서를 작성한다.")
    @PostMapping
    public ResponseEntity<String> createShippingInstruction(
            @RequestBody ShippingInstructionRequest shippingInstructionRequest
    ) {

        shippingInstructionApplicationService.createShippingInstruction(shippingInstructionRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("출하지시서 작성성공");
    }

    // 출하지시서 수정
    @Operation(summary = "출하지시서 수정", description = "출하지시서를 수정한다.")
    @PutMapping("/{shippingInstructionSeq}")
    public ResponseEntity<String> updateShippingInstruction(
            @PathVariable Long shippingInstructionSeq,
            @RequestBody ShippingInstructionRequest shippingInstructionRequest
    ) {

        shippingInstructionApplicationService.updateShippingInstruction(shippingInstructionSeq, shippingInstructionRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("출하지시서 수정성공");
    }

    // 출하지시서 상태 변경
    @Operation(summary = "출하지시서 상태 변경", description = "출하지시서의 상태를 변경한다.")
    @PutMapping("/{shippingInstructionSeq}/status")
    public ResponseEntity<String> updateShippingInstructionStatus(
            @PathVariable Long shippingInstructionSeq
    ) {

        shippingInstructionApplicationService.updateShippingInstructionStatus(shippingInstructionSeq);

        return ResponseEntity.status(HttpStatus.CREATED).body("출하지시서 상태 변경 성공");
    }

    // 출하지시서 수정
    @Operation(summary = "출하지시서 삭제", description = "출하지시서를 삭제한다.")
    @DeleteMapping("/{shippingInstructionSeq}")
    public ResponseEntity<String> deleteShippingInstruction(
            @PathVariable Long shippingInstructionSeq
    ) {

        shippingInstructionApplicationService.deleteShippingInstruction(shippingInstructionSeq);

        return ResponseEntity.status(HttpStatus.OK).body("출하지시서 삭제 성공");
    }
}
