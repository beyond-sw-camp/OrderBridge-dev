package error.pirate.backend.warehouse.query.mapper;

import error.pirate.backend.warehouse.query.dto.WarehouseResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WarehouseMapper {

    List<WarehouseResponse> findWarehouseListByFilter(
            @Param("warehouseName") String warehouseName,
            @Param("warehouseType") String warehouseType,
            @Param("sortBy") String sortBy,
            @Param("sortDirection") String sortDirection,
            @Param("offset") int offset,
            @Param("size") int size
    );
}
