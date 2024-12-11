package error.pirate.backend.item.query.controller;

import error.pirate.backend.item.query.dto.ItemDetailResponse;
import error.pirate.backend.item.query.dto.ItemFilterRequest;
import error.pirate.backend.item.query.dto.ItemResponse;
import error.pirate.backend.item.query.service.ItemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Tag(name = "품목 관리", description = "품목 관리 API")
public class ItemQueryController {

    private final ItemQueryService itemQueryService;

    @GetMapping
    @Operation(summary = "품목 조회", description = "품목을 조회한다.")
    public ResponseEntity<List<ItemResponse>> readItemList(ItemFilterRequest itemFilterRequest) {
        List<ItemResponse> itemList = itemQueryService.readItemList(itemFilterRequest);
        return ResponseEntity.ok(itemList);
    }


    @GetMapping("/{itemSeq}")
    @Operation(summary = "품목 상세조회", description = "품목을 상세 조회한다.")
    public ResponseEntity<ItemDetailResponse> readItem(@PathVariable Long itemSeq) {
        ItemDetailResponse itemDetailResponse = itemQueryService.readItem(itemSeq);

        return ResponseEntity.ok(itemDetailResponse);
    }
}

