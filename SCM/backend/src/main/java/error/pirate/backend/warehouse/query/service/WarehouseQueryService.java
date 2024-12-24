package error.pirate.backend.warehouse.query.service;

import error.pirate.backend.warehouse.query.dto.WarehouseFilterRequest;
import error.pirate.backend.warehouse.query.dto.WarehouseListResponse;
import error.pirate.backend.warehouse.query.dto.WarehouseResponse;
import error.pirate.backend.warehouse.query.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseQueryService {
    private final WarehouseMapper warehouseMapper;

    public WarehouseListResponse readWarehouseList(WarehouseFilterRequest filterRequest) {
        int offset = (filterRequest.getPage() - 1) * filterRequest.getSize();

        List<WarehouseResponse> warehouses = warehouseMapper.findWarehouseListByFilter(
                filterRequest.getWarehouseName(),
                filterRequest.getWarehouseType(),
                offset,
                filterRequest.getSize()
        );

        int totalCount = warehouseMapper.countWarehousesByFilter(
                filterRequest.getWarehouseName(),
                filterRequest.getWarehouseType()
        );

        return new WarehouseListResponse(warehouses, totalCount);
    }
    public WarehouseResponse findWareHouseDetail(Long warehouseSeq) {

        return warehouseMapper.findWarehouseDetail(warehouseSeq);
    }
}

