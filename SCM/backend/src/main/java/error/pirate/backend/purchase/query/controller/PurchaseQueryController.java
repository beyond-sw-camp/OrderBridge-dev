package error.pirate.backend.purchase.query.controller;

import error.pirate.backend.purchase.query.dto.PurchaseRequest;
import error.pirate.backend.purchase.query.dto.PurchaseResponsePagination;
import error.pirate.backend.purchase.query.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchase")
@Tag(name = "구매 API", description = "구매 API")
public class PurchaseQueryController {

    private final PurchaseService purchaseService;

    @GetMapping
    @Operation(summary = "구매서 조회")
    public ResponseEntity<PurchaseResponsePagination> readPurchaseList(PurchaseRequest request) {
        return ResponseEntity.ok(purchaseService.readPurchaseList(request));
    }

    @GetMapping("/excelDown")
    @Operation(summary = "구매서 엑셀다운")
    public ResponseEntity<byte[]> purchaseOrderExcelDown(PurchaseRequest request) {
        HttpHeaders headersResponse = new HttpHeaders();
        String fileName = URLEncoder.encode("구매서[" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "].xlsx", StandardCharsets.UTF_8);
        headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headersResponse)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(purchaseService.purchaseExcelDown(request));
    }

}
