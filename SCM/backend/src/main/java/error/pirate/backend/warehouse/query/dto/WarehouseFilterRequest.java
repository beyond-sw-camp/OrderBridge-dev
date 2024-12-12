package error.pirate.backend.warehouse.query.dto;

import lombok.Data;

@Data
public class WarehouseFilterRequest {
    private String warehouseName; // 창고 이름 필터
    private String warehouseType; // 창고 유형 필터
    private String sortBy; // 정렬 기준
    private String sortDirection; // 정렬 방향 (ASC/DESC)
    private int page = 0; // 기본값 페이지 번호
    private int size = 10; // 기본값 페이지 크기
}
