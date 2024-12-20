package error.pirate.backend.shippingSlip.query.controller;

import error.pirate.backend.shippingSlip.query.dto.*;
import error.pirate.backend.shippingSlip.query.service.ShippingSlipQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "출하전표")
@RestController
@RequiredArgsConstructor    // final 을 받은 필드의 생성자를 주입
@RequestMapping("/api/v1/shipping-slip")
@Slf4j
public class ShippingSlipQueryController {
    private final ShippingSlipQueryService shippingSlipQueryService;

    @Operation(summary = "출하전표 조회", description = "출하전표 조회")
    @GetMapping
    public ResponseEntity<ShippingSlipListResponse> readShippingSlipList(
            @ModelAttribute ShippingSlipListRequest request
    ) {
        ShippingSlipListResponse response
                = shippingSlipQueryService.readShippingSlipList(request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "출하전표 상세조회", description = "출하전표 상세조회")
    @GetMapping("/{shippingSlipSeq}")
    public ResponseEntity<ShippingSlipResponse> readShippingSlip(
            @PathVariable long shippingSlipSeq
    ) {
        ShippingSlipResponse response
                = shippingSlipQueryService.readShippingSlip(shippingSlipSeq);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/excelDown")
    @Operation(summary = "출하전표 엑셀 다운")
    public ResponseEntity<byte[]> shippingInstructionExcelDown(ShippingSlipListRequest request) {
        byte[] excelData = shippingSlipQueryService.shippingSlipExcelDown(request);

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(excelData);
    }

    @Operation(summary = "출하전표 현황 조회", description = "출하전표 현황 조회")
    @GetMapping("/situation")
    public ResponseEntity<List<ShippingSlipSituationResponse>> readShippingSlipSituation(
            @ModelAttribute ShippingSlipSituationRequest request
    ) {
        List<ShippingSlipSituationResponse> response
                = shippingSlipQueryService.readShippingSlipSituation(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/situation/excelDown")
    @Operation(summary = "출하전표 현황 엑셀 다운")
    public ResponseEntity<byte[]> shippingSlipSituationExcelDown(
            @ModelAttribute ShippingSlipSituationRequest request) {

        byte[] excelData = shippingSlipQueryService.shippingSlipSituationExcelDown(request);

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(excelData);
    }
}
