package error.pirate.backend.item.query.controller;

import error.pirate.backend.item.command.application.dto.BomItemDTO;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import error.pirate.backend.item.query.dto.*;
import error.pirate.backend.item.query.service.ItemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Tag(name = "Item", description = "품목 관리")
@Slf4j
public class ItemQueryController {

    private final ItemQueryService itemQueryService;

    @GetMapping
    @Operation(summary = "품목 조회", description = "품목을 조회한다.")
    public ResponseEntity<Map<String, Object>> readItemList(
            @RequestParam(defaultValue = "0") int page, // 기본값 0
            @RequestParam(defaultValue = "8") int size, // 기본값 8
            @RequestParam(required = false) String itemName, // 검색어 필터
            @RequestParam(required = false) List<ItemDivision> itemDivisions, // 품목 구분 필터
            @RequestParam(required = false) Integer minExpirationHour, // 최소 유통기한
            @RequestParam(required = false) Integer maxExpirationHour  // 최대 유통기한
    ) {
        // 페이지 번호가 음수로 들어오는 경우 기본값으로 보정
        if (page < 0) page = 0;
        if (size <= 0) size = 8;

        log.info("품목 조회");
        // 필터링 및 페이징 요청 객체 생성
        ItemFilterRequest itemFilterRequest = new ItemFilterRequest(page, size, itemName, itemDivisions, minExpirationHour, maxExpirationHour);

        // 서비스 호출로 데이터 조회
        Map<String, Object> response = itemQueryService.readItemList(itemFilterRequest);

        // 결과 반환
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unit")
    @Operation(summary = "품목 단위 조회", description = "등록 가능한 품목 단위를 조회한다.")
    public ResponseEntity<List<ItemUnitResponse>> readItemUnits() {
        List<ItemUnitResponse> itemUnits = itemQueryService.getItemUnits();
        return ResponseEntity.ok(itemUnits);
    }

    @GetMapping("/division")
    @Operation(summary = "품목 구분 조회", description = "등록 가능한 품목 구분을 조회한다.")
    public ResponseEntity<List<ItemDivisionResponse>> getItemDivisions() {
        List<ItemDivisionResponse> divisions = itemQueryService.getAllItemDivisions();
        return ResponseEntity.ok(divisions);
    }

    @GetMapping("/{itemSeq}")
    @Operation(summary = "품목 상세조회", description = "품목을 상세 조회한다.")
    public ResponseEntity<ItemDetailResponse> readItem(@PathVariable Long itemSeq) {
        ItemDetailResponse itemDetailResponse = itemQueryService.readItem(itemSeq);

        return ResponseEntity.ok(itemDetailResponse);
    }

    @GetMapping("/item-division")
    @Operation(summary = "품목 분류 조회")
    public ResponseEntity<List<ItemDivision.ItemDivisionResponse>> readItemDivision() {
        return ResponseEntity.ok(ItemDivision.readItemDivisionList());
    }

    @GetMapping("/bom-item/{itemSeq}")
    @Operation(summary = "bom 품목 조회")
    public ResponseEntity<List<BomItemDTO>> readBomItems(@PathVariable Long itemSeq) {
        List<BomItemDTO> bomItems = itemQueryService.readBomItems(itemSeq);
        return ResponseEntity.ok(bomItems);
    }
}

