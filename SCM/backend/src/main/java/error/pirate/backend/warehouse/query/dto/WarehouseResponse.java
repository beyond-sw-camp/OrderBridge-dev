package error.pirate.backend.warehouse.query.dto;

import error.pirate.backend.warehouse.command.domain.aggregate.entity.WarehouseType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseResponse {
    private Long warehouseSeq; // 창고 번호
    private String warehouseName; // 창고명
    private WarehouseType warehouseType; // 창고 구분
    private String warehouseNote; // 창고 비고
    private LocalDateTime warehouseRegDate; // 창고 등록일
    private LocalDateTime warehouseModDate; // 창고 수정일
}
