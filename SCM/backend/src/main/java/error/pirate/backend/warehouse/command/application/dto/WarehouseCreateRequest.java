package error.pirate.backend.warehouse.command.application.dto;

import error.pirate.backend.warehouse.command.domain.aggregate.entity.WarehouseType;
import lombok.Data;

@Data
public class WarehouseCreateRequest {
    private Long userSeq; // 창고 담당자 번호
    private String warehouseName; // 창고명
    private WarehouseType warehouseType; // 창고 구분
    private String warehouseNote; // 창고 비고
}
