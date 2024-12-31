package error.pirate.backend.warehouse.command.application.controller;

import error.pirate.backend.warehouse.command.application.dto.WarehouseCreateRequest;
import error.pirate.backend.warehouse.command.application.dto.WarehouseResponse;
import error.pirate.backend.warehouse.command.application.dto.WarehouseUpdateRequest;
import error.pirate.backend.warehouse.command.application.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Tag(name = "Warehouse", description = "창고 관리")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(summary = "창고 등록", description = "창고를 등록한다.")
    public ResponseEntity<WarehouseResponse> createWarehouse(@RequestBody WarehouseCreateRequest request) {
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{warehouseSeq}")
    @Operation(summary = "창고 수정", description = "창고 정보를 수정합니다.")
    public ResponseEntity<Void> updateWarehouse(@PathVariable Long warehouseSeq, @Valid @RequestBody WarehouseUpdateRequest request) {
        warehouseService.updateWarehouse(warehouseSeq, request);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{warehouseSeq}")
    @Operation(summary = "창고 삭제", description = "창고 상태값을 변경해 삭제 처리합니다.")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long warehouseSeq) {
        warehouseService.deleteWarehouse(warehouseSeq);
        return ResponseEntity.noContent().build();
    }
}

