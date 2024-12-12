package error.pirate.backend.workOrder.command.application.controller;

import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.application.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workOrder")
@Slf4j
@Tag(name = "Work Order", description = "작업지시서")
public class WorkOrderCommandController {

    private final WorkOrderService workOrderService;

    @PostMapping
    @Operation(summary = "작업지시서 등록", description = "작업지시서를 등록한다.")
    public ResponseEntity<Void> createWorkOrder(@Valid @RequestBody CreateWorkOrderRequest request) {
        log.info("POST /api/v1/workOrder 작업지시서 등록 요청 - request:{}", request);
        workOrderService.createWorkOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
