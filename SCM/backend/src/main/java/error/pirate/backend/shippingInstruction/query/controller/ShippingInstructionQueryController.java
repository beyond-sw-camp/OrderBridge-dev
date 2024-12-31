package error.pirate.backend.shippingInstruction.query.controller;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingAddress;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import error.pirate.backend.shippingInstruction.query.dto.*;
import error.pirate.backend.shippingInstruction.query.service.ShippingInstructionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor    // final 을 받은 필드의 생성자를 주입
@RequestMapping("/api/v1/shipping-instruction")
@Slf4j
@Tag(name = "Shipping Instruction", description = "출하지시서")
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

    @Operation(summary = "출하지시서 현황 조회", description = "출하지시서 현황 조회")
    @GetMapping("/situation")
    public ResponseEntity<List<ShippingInstructionSituationResponse>> readShippingInstructionSituation(
            @ModelAttribute ShippingInstructionSituationRequest request
    ) {
        List<ShippingInstructionSituationResponse> response
                = shippingInstructionQueryService.readShippingInstructionSituation(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/excel")
    @Operation(summary = "출하지시서 엑셀")
    public ResponseEntity<byte[]> shippingInstructionExcel(
            @ModelAttribute ShippingInstructionListRequest request) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_출하지시서.xlsx"
                , StandardCharsets.UTF_8));

        byte[] excelData = shippingInstructionQueryService.shippingInstructionExcel(request);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/status")
    @Operation(summary = "출하지시서 상태 분류 조회")
    public ResponseEntity<List<ShippingInstructionStatus.ShippingInstructionStatusResponse>> readShippingInstructionStatus() {
        return ResponseEntity.ok(ShippingInstructionStatus.readShippingInstructionStatusList());
    }

    @GetMapping("/address")
    @Operation(summary = "출하지시서 주소 리스트 조회")
    public ResponseEntity<List<ShippingAddress.ShippingInstructionAddressResponse>> readShippingInstructionAddress() {
        return ResponseEntity.ok(ShippingAddress.readShippingInstructionAddressList());
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
