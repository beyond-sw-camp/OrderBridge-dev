package error.pirate.backend.warehouse.command.application.controller;

import error.pirate.backend.warehouse.command.application.dto.WarehouseCreateRequest;
import error.pirate.backend.warehouse.command.application.dto.WarehouseDTO;
import error.pirate.backend.warehouse.command.application.dto.WarehouseResponse;
import error.pirate.backend.warehouse.command.application.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Tag(name = "창고 관리", description = "창고 관리 API")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(summary = "창고 등록", description = "창고를 등록한다.")
    public ResponseEntity<WarehouseResponse> createWarehouse(@RequestBody WarehouseCreateRequest request) {
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return ResponseEntity.ok(response);
    }
}

