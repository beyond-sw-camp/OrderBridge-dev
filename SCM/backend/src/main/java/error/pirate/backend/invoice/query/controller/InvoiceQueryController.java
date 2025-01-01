package error.pirate.backend.invoice.query.controller;

import error.pirate.backend.invoice.command.domain.aggregate.entity.InvoiceStatus;
import error.pirate.backend.invoice.query.dto.InvoiceListResponse;
import error.pirate.backend.invoice.query.dto.InvoiceResponse;
import error.pirate.backend.invoice.query.dto.InvoiceSituationResponse;
import error.pirate.backend.invoice.query.service.InvoiceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/v1/invoice")
@Tag(name = "Invoice", description = "거래 명세서")
public class InvoiceQueryController {

    private final InvoiceQueryService invoiceQueryService;

    @GetMapping("")
    @Operation(summary = "거래 명세서 목록 조회")
    public ResponseEntity<InvoiceListResponse> readInvoiceList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName) {

        return ResponseEntity.ok(invoiceQueryService.readInvoiceList(
                page, size, startDate, endDate, clientName));
    }

    @GetMapping("/{invoiceSeq}")
    @Operation(summary = "거래 명세서 상세 조회")
    public ResponseEntity<InvoiceResponse> readInvoice(
            @PathVariable Long invoiceSeq) {

        return ResponseEntity.ok(invoiceQueryService.readInvoice(invoiceSeq));
    }

    @GetMapping("/situation")
    @Operation(summary = "거래 명세서 현황 조회")
    public ResponseEntity<InvoiceSituationResponse> readInvoiceSituation(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName) {

        return ResponseEntity.ok(invoiceQueryService.readInvoiceSituation(
                startDate, endDate, clientName));
    }

    @GetMapping("/status")
    @Operation(summary = "거래 명세서 상태 분류 조회")
    public ResponseEntity<List<InvoiceStatus.InvoiceStatusResponse>> readInvoiceStatus() {
        return ResponseEntity.ok(InvoiceStatus.readInvoiceStatusList());
    }

    @GetMapping("/excel")
    @Operation(summary = "거래 명세서 엑셀 다운로드")
    public ResponseEntity<byte[]> readInvoiceExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_거래명세서.xlsx"
                , StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(invoiceQueryService.readInvoiceExcel(startDate, endDate, clientName));
    }

    @GetMapping("/situation/excel")
    @Operation(summary = "거래 명세서 현황 엑셀 다운로드")
    public ResponseEntity<byte[]> readInvoiceSituationExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_거래명세서_현황.xlsx"
                , StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(invoiceQueryService.readInvoiceSituationExcel(startDate, endDate, clientName));
    }
}
