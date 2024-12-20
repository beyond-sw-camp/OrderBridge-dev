package error.pirate.backend.warehouse.query.service;

import error.pirate.backend.warehouse.query.dto.WarehouseFilterRequest;
import error.pirate.backend.warehouse.query.dto.WarehouseResponse;
import error.pirate.backend.warehouse.query.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseQueryService {

    private final WarehouseMapper warehouseMapper;

    // 페이징 데이터 조회
    public List<WarehouseResponse> readWarehouseList(WarehouseFilterRequest filterRequest) {
        int offset = filterRequest.getPage() * filterRequest.getSize();
        return warehouseMapper.findWarehouseListByFilter(
                filterRequest.getWarehouseName(),
                filterRequest.getWarehouseType(),
                offset,
                filterRequest.getSize()
        );
    }

    // 모든 데이터 조회
    public List<WarehouseResponse> readAllWarehouses() {
        return warehouseMapper.findAllWarehouses();
    }
}

