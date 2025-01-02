package error.pirate.backend.warehouse.command.application.service;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.common.SecurityUtil;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import error.pirate.backend.warehouse.command.application.dto.WarehouseCreateRequest;
import error.pirate.backend.warehouse.command.application.dto.WarehouseResponse;
import error.pirate.backend.warehouse.command.application.dto.WarehouseUpdateRequest;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static error.pirate.backend.warehouse.command.domain.aggregate.entity.QWarehouse.warehouse;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @Transactional
    public WarehouseResponse createWarehouse(WarehouseCreateRequest request) {
        // 사용자 조회
        User user = userRepository.findByUserEmployeeNo(securityUtil.getCurrentUserEmployeeNo())
                .orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));

        // 요청을 Warehouse 엔티티로 매핑
        Warehouse warehouse = modelMapper.map(request, Warehouse.class);
        warehouse.specifyUser(user);

        // 저장
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        // 엔티티를 응답 DTO로 매핑
        return modelMapper.map(savedWarehouse, WarehouseResponse.class);
    }
    @Transactional
    public void updateWarehouse(Long warehouseSeq, WarehouseUpdateRequest request) {
        Warehouse warehouse = warehouseRepository.findById(warehouseSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));

        warehouse.updateWarehouse(request);
    }
    @Transactional
    public void deleteWarehouse(Long warehouseSeq) {
        Warehouse warehouse = warehouseRepository.findById(warehouseSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));

        warehouse.delete(); // 상태값 변경
    }
}
