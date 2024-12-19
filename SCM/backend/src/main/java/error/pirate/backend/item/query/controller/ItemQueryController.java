package error.pirate.backend.item.query.controller;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import error.pirate.backend.item.query.dto.ItemDetailResponse;
import error.pirate.backend.item.query.dto.ItemFilterRequest;
import error.pirate.backend.item.query.dto.ItemResponse;
import error.pirate.backend.item.query.service.ItemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Tag(name = "품목 관리", description = "품목 관리 API")
public class ItemQueryController {

    private final ItemQueryService itemQueryService;

    @GetMapping
    @Operation(summary = "품목 조회", description = "품목을 조회한다.")
    public ResponseEntity<Map<String, Object>> readItemList(
            @RequestParam(defaultValue = "0") int page, // 기본값 0
            @RequestParam(defaultValue = "8") int size, // 기본값 8
            @RequestParam(required = false) String itemName, // 검색어 필터
            @RequestParam(required = false) ItemDivision itemDivisions, // 품목 구분 필터
            @RequestParam(required = false) Integer minExpirationHour, // 최소 유통기한
            @RequestParam(required = false) Integer maxExpirationHour  // 최대 유통기한
    ) {
        // 페이지 번호가 음수로 들어오는 경우 기본값으로 보정
        if (page < 0) page = 0;
        if (size <= 0) size = 8;


        // 필터링 및 페이징 요청 객체 생성
        ItemFilterRequest itemFilterRequest = new ItemFilterRequest(page, size, itemName, itemDivisions, minExpirationHour, maxExpirationHour);

        // 서비스 호출로 데이터 조회
        Map<String, Object> response = itemQueryService.readItemList(itemFilterRequest);

        // 결과 반환
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{itemSeq}")
    @Operation(summary = "품목 상세조회", description = "품목을 상세 조회한다.")
    public ResponseEntity<ItemDetailResponse> readItem(@PathVariable Long itemSeq) {
        ItemDetailResponse itemDetailResponse = itemQueryService.readItem(itemSeq);

        return ResponseEntity.ok(itemDetailResponse);
    }


}

