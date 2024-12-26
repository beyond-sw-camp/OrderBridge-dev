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
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 전체 개수를 가져오는 메서드 추가
    int countWarehousesByFilter(
            @Param("warehouseName") String warehouseName,
            @Param("warehouseType") String warehouseType
    );

    // 모든 데이터 조회
    List<WarehouseResponse> findAllWarehouses();

    WarehouseResponse findWarehouseDetail(
            @Param("warehouseSeq") Long warehouseSeq);
}

