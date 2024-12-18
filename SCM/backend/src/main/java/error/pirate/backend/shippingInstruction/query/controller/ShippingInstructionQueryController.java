package error.pirate.backend.shippingInstruction.query.controller;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import error.pirate.backend.shippingInstruction.query.dto.*;
import error.pirate.backend.shippingInstruction.query.service.ShippingInstructionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "출하지시서")
@RestController
@RequiredArgsConstructor    // final 을 받은 필드의 생성자를 주입
@RequestMapping("/api/v1/shipping-instruction")
@Slf4j
public class ShippingInstructionQueryController {
    private final ShippingInstructionQueryService shippingInstructionQueryService;

    @Operation(summary = "출하지시서 조회", description = "출하지시서 조회")
    @GetMapping
    public ResponseEntity<ShippingInstructionListResponse> readShippingInstructionList(
            @ModelAttribute ShippingInstructionListRequest request
    ) {
        ShippingInstructionListResponse response
                = shippingInstructionQueryService.readShippingInstructionList(request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "출하지시서 상세조회", description = "출하지시서 상세조회")
    @GetMapping("/{shippingInstructionSeq}")
    public ResponseEntity<ShippingInstructionResponse> readShippingInstruction(
            @PathVariable long shippingInstructionSeq
    ) {
        ShippingInstructionResponse response
                = shippingInstructionQueryService.readShippingInstruction(shippingInstructionSeq);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/excelDown")
    @Operation(summary = "출하지시서 엑셀 다운")
    public ResponseEntity<byte[]> shippingInstructionExcelDown(
            @ModelAttribute ShippingInstructionListRequest request) {

        byte[] excelData = shippingInstructionQueryService.shippingInstructionExcelDown(request);

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(excelData);
    }

    @Operation(summary = "출하지시서 현황 조회", description = "출하지시서 현황 조회")
    @GetMapping("/situation")
    public ResponseEntity<List<ShippingInstructionSituationResponse>> readShippingInstructionSituation(
            @ModelAttribute ShippingInstructionSituationRequest request
    ) {
        List<ShippingInstructionSituationResponse> response
                = shippingInstructionQueryService.readShippingInstructionSituation(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/situation/excelDown")
    @Operation(summary = "출하지시서 현황 엑셀 다운")
    public ResponseEntity<byte[]> shippingInstructionSituationExcelDown(
            @ModelAttribute ShippingInstructionSituationRequest request) {

        byte[] excelData = shippingInstructionQueryService.shippingInstructionSituationExcelDown(request);

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(excelData);
    }

    @GetMapping("/status")
    @Operation(summary = "견적서 상태 분류 조회")
    public ResponseEntity<List<ShippingInstructionStatus.ShippingInstructionStatusResponse>> readShippingInstructionStatus() {
        return ResponseEntity.ok(ShippingInstructionStatus.readShippingInstructionList());
    }

    @Operation(summary = "출하지시서 등록 시 남아있는 주문서 품목 수량 조회", description = "출하지시서 등록 시 남아있는 주문서 품목 수량 조회")
    @GetMapping("/quantity/{salesOrderSeq}")
    public ResponseEntity<List<Integer>> readRemainingQuantity(
            @PathVariable long salesOrderSeq
    ) {
        List<Integer> response
                = shippingInstructionQueryService.readRemainingQuantity(salesOrderSeq);

        return ResponseEntity.ok(response);
    }
}
