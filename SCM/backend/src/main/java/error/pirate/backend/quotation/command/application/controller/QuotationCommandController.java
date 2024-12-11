package error.pirate.backend.quotation.command.application.controller;

import error.pirate.backend.quotation.command.application.dto.CreateQuotationRequest;
import error.pirate.backend.quotation.command.application.dto.UpdtaeQuotationRequost;
import error.pirate.backend.quotation.command.application.service.QuotationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quotation")
@Tag(name = "Quotation", description = "견적서")
public class QuotationCommandController {

    private final QuotationCommandService quotationCommandService;

    @PostMapping("")
    @Operation(summary = "견적서 등록")
    public ResponseEntity<Void> createQuotation(@RequestBody CreateQuotationRequest request) {

        quotationCommandService.createQuotation(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{quotationSeq}")
    @Operation(summary = "견적서 수정")
    public ResponseEntity<Void> updateQuotation(
            @PathVariable Long quotationSeq,
            @RequestBody UpdtaeQuotationRequost request) {

        quotationCommandService.updateQuotation(quotationSeq, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
