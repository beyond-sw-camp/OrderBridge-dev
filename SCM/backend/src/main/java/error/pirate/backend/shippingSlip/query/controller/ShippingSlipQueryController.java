package error.pirate.backend.shippingSlip.query.controller;

import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionListRequest;
import error.pirate.backend.shippingSlip.query.dto.*;
import error.pirate.backend.shippingSlip.query.service.ShippingSlipQueryService;
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

    @Operation(summary = "출하전표 현황 조회", description = "출하전표 현황 조회")
    @GetMapping("/situation")
    public ResponseEntity<ShippingSlipSituationResponse> readShippingSlipSituation(
            @ModelAttribute ShippingSlipSituationRequest request
    ) {
        ShippingSlipSituationResponse response
                = shippingSlipQueryService.readShippingSlipSituation(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/excelDown")
    @Operation(summary = "출하전표 엑셀 다운")
    public ResponseEntity<byte[]> shippingInstructionExcelDown(ShippingSlipListRequest request) {
        HttpHeaders headersResponse = new HttpHeaders();
        String fileName = URLEncoder.encode("출하전표[" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "].xlsx", StandardCharsets.UTF_8);
        headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headersResponse)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(shippingSlipQueryService.shippingSlipExcelDown(request));
    }
}
