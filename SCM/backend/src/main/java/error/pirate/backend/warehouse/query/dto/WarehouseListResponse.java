// WarehouseListResponse.java
package error.pirate.backend.warehouse.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WarehouseListResponse {
    private List<WarehouseResponse> warehouses;
    private int totalCount;
}