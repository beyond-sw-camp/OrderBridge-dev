package error.pirate.backend.shippingInstruction.command.application.controller;

import error.pirate.backend.security.AuthUtil;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import error.pirate.backend.shippingInstruction.command.application.service.ShippingInstructionApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/shipping-instruction")
@Slf4j
@Tag(name = "Shipping Instruction", description = "출하지시서")
public class ShippingInstructionCommandController {

    private final ShippingInstructionApplicationService shippingInstructionApplicationService;

    // 출하지시서 작성
    @Operation(summary = "출하지시서 작성", description = "출하지시서를 작성한다.")
    @PostMapping
    public ResponseEntity<String> createShippingInstruction(
            @RequestBody ShippingInstructionRequest shippingInstructionRequest
    ) {
        String userNo = AuthUtil.getAuthUser();
        shippingInstructionApplicationService.createShippingInstruction(shippingInstructionRequest, userNo);

        return ResponseEntity.status(HttpStatus.CREATED).body("출하지시서 작성성공");
    }

    // 출하지시서 수정
    @Operation(summary = "출하지시서 수정", description = "출하지시서를 수정한다.")
    @PutMapping("/{shippingInstructionSeq}")
    public ResponseEntity<String> updateShippingInstruction(
            @PathVariable Long shippingInstructionSeq,
            @RequestBody ShippingInstructionRequest shippingInstructionRequest
    ) {
        String userNo = AuthUtil.getAuthUser();
        shippingInstructionApplicationService.updateShippingInstruction(shippingInstructionSeq, shippingInstructionRequest, userNo);

        return ResponseEntity.status(HttpStatus.OK).body("출하지시서 수정성공");
    }

    // 출하지시서 상태 변경
    @Operation(summary = "출하지시서 결재 상태 변경", description = "출하지시서의 상태를 결재후 상태로 변경한다.")
    @PutMapping("/approval/{shippingInstructionSeq}")
    public ResponseEntity<String> updateShippingInstructionStatus(
            @PathVariable Long shippingInstructionSeq
    ) {
        shippingInstructionApplicationService.updateShippingInstructionApprovalStatus(shippingInstructionSeq);

        return ResponseEntity.status(HttpStatus.OK).body("출하지시서 결재 상태 변경 성공");
    }

    // 출하지시서 삭제
    @Operation(summary = "출하지시서 삭제", description = "출하지시서를 삭제한다.")
    @DeleteMapping("/{shippingInstructionSeq}")
    public ResponseEntity<String> deleteShippingInstruction(
            @PathVariable Long shippingInstructionSeq
    ) {
        String userNo = AuthUtil.getAuthUser();
        shippingInstructionApplicationService.deleteShippingInstruction(shippingInstructionSeq, userNo);

        return ResponseEntity.status(HttpStatus.OK).body("출하지시서 삭제 성공");
    }
}
