package error.pirate.backend.warehouse.query.dto;

import error.pirate.backend.warehouse.command.domain.aggregate.entity.WarehouseType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WarehouseDTO {
    private Long warehouseSeq;
    private String warehouseName;
    private WarehouseType warehouseType;
    private LocalDateTime warehouseRegDate;
    private String warehouseNote;
}
