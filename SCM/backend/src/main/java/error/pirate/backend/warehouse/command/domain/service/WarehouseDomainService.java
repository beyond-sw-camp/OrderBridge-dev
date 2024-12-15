package error.pirate.backend.warehouse.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseDomainService {

    private final WarehouseRepository warehouseRepository;

    public Warehouse findById(Long warehouseSeq) {
        return warehouseRepository.findById(warehouseSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));
    }
}
