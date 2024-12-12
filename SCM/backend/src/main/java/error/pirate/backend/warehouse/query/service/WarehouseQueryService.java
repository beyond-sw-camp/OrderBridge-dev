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

    public List<WarehouseResponse> readWarehouseList(WarehouseFilterRequest filterRequest) {

        int offset = filterRequest.getPage() * filterRequest.getSize();
        return warehouseMapper.findWarehouseListByFilter(
                filterRequest.getWarehouseName(),
                filterRequest.getWarehouseType(),
                filterRequest.getSortBy(),
                filterRequest.getSortDirection(),
                offset,
                filterRequest.getSize()
        );
    }
}
