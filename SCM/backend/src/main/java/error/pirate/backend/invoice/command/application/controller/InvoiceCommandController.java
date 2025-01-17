package error.pirate.backend.invoice.command.application.controller;

import error.pirate.backend.common.SecurityUtil;
import error.pirate.backend.invoice.command.application.dto.CreateInvoiceRequest;
import error.pirate.backend.invoice.command.application.dto.UpdateInvoiceRequest;
import error.pirate.backend.invoice.command.application.service.InvoiceCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoice")
@Tag(name = "Invoice", description = "거래 명세서")
public class InvoiceCommandController {

    private final SecurityUtil securityUtil;
    private final InvoiceCommandService invoiceCommandService;

    @PostMapping("")
    @Operation(summary = "거래 명세서 등록")
    public ResponseEntity<Void> createInvoice(@RequestBody CreateInvoiceRequest createInvoiceRequest) {

        invoiceCommandService.createInvoice(createInvoiceRequest, securityUtil.getCurrentUserEmployeeNo());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{invoiceSeq}")
    @Operation(summary = "거래 명세서 수정")
    public ResponseEntity<Void> updateInvoice(
            @PathVariable Long invoiceSeq,
            @RequestBody UpdateInvoiceRequest request) {

        invoiceCommandService.updateInvoice(invoiceSeq, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{invoiceSeq}")
    @Operation(summary = "거래 명세서 삭제")
    public ResponseEntity<Void> deleteInvoice(
            @PathVariable Long invoiceSeq) {

        invoiceCommandService.deleteInvoice(invoiceSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
