package error.pirate.backend.productionDisbursement.query.controller;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.productionDisbursement.query.dto.ProductionDisbursementListResponse;
import error.pirate.backend.productionDisbursement.query.dto.ProductionDisbursementResponse;
import error.pirate.backend.productionDisbursement.query.dto.ProductionDisbursementSituationResponse;
import error.pirate.backend.productionDisbursement.query.service.ProductionDisbursementQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productionDisbursement")
@Slf4j
@Tag(name = "Production Disbursement", description = "생산불출")
public class ProductionDisbursementQueryController {

    private final ProductionDisbursementQueryService productionDisbursementQueryService;

    /* 목록조회 */
    @GetMapping
    @Operation(summary = "생산불출 목록조회", description = "생산불출 목록을 조회한다.")
    public ResponseEntity<ProductionDisbursementListResponse> readProductionDisbursementList (
            @RequestParam(required = false) String factoryName,
            @RequestParam(required = false) List<ProductionDisbursementStatus> productionDisbursementStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
            ) {
        log.info("-------------- GET /api/v1/productionDisbursement 생산불출 목록조회 요청 --------------");
        ProductionDisbursementListResponse response =
                productionDisbursementQueryService.readProductionDisbursementList(factoryName, productionDisbursementStatus, startDate, endDate, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 상세조회 */
    @GetMapping("/{productionDisbursementSeq}")
    @Operation(summary = "작업지시서 상세조회", description = "작업지시서 상세내용을 조회한다.")
    public ResponseEntity<ProductionDisbursementResponse> readProductionDisbursement(@PathVariable Long productionDisbursementSeq) {
        log.info("-------------- GET /api/v1/productionDisbursement/{} 작업지시서 상세조회 요청 --------------", productionDisbursementSeq);
        ProductionDisbursementResponse response = productionDisbursementQueryService.readProductionDisbursement(productionDisbursementSeq);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 목록조회 엑셀 다운로드 */
    @GetMapping("/excelDownload")
    @Operation(summary = "생산불출 목록조회 엑셀 다운로드", description = "생산불출 목록을 엑셀로 다운로드한다.")
    public ResponseEntity<byte[]> productionDisbursementExcelDown(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String factoryName,
            @RequestParam(required = false) List<ProductionDisbursementStatus> productionDisbursementStatus
    ) {
        log.info("-------------- GET /api/v1/productionDisbursement/excelDownload 생산불출 엑셀 다운로드 요청 --------------");

        byte[] excelData = productionDisbursementQueryService.readProductionDisbursementExcel(startDate, endDate, factoryName, productionDisbursementStatus);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "생산불출.xlsx",
                StandardCharsets.UTF_8));
        httpHeaders.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    /* 현황조회 */
    @GetMapping("/currentSituation")
    @Operation(summary = "생산불출 현황조회", description = "생산불출 현황을 조회한다.")
    public ResponseEntity<ProductionDisbursementSituationResponse> readProductionDisbursementSituation (
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String factoryName
    ) {
        log.info("-------------- GET /api/v1/productionDisbursement/currentSituation 생산불출 현황조회 요청 --------------");
        ProductionDisbursementSituationResponse response
                = productionDisbursementQueryService.readProductionDisbursementSituation(startDate, endDate, storeName, factoryName);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /* 현황 엑셀 다운로드 */
    @GetMapping("/situation/excelDownload")
    @Operation(summary = "생산불출 목록조회 엑셀 다운로드", description = "생산불출 목록을 엑셀로 다운로드한다.")
    public ResponseEntity<byte[]> productionDisbursementSituationExcelDown(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String factoryName,
            @RequestParam(required = false) String storeName
    ) {
        log.info("-------------- GET /api/v1/productionDisbursement/situation/excelDownload 생산불출 엑셀 다운로드 요청 --------------");

        byte[] excelData = productionDisbursementQueryService.readProductionDisbursementSituationExcel(startDate, endDate, factoryName, storeName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "생산불출현황.xlsx",
                StandardCharsets.UTF_8));
        httpHeaders.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

}
