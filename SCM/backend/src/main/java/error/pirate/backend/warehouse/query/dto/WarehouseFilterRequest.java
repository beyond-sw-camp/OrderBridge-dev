package error.pirate.backend.warehouse.query.dto;

import lombok.Data;

@Data
public class WarehouseFilterRequest {
    private String warehouseName; // 창고 이름 필터
    private String warehouseType; // 창고 유형 필터
    private int page = 1; // 기본값 페이지 번호 (1부터 시작)
    private int size = 10; // 기본값 페이지 크기
}