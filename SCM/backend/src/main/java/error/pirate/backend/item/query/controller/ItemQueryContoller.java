package error.pirate.backend.item.query.controller;

import error.pirate.backend.item.query.dto.ItemFilterRequest;
import error.pirate.backend.item.query.dto.ItemDTO;
import error.pirate.backend.item.query.service.ItemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Tag(name = "품목 관리", description = "품목 관리 API")
public class ItemQueryContoller {

    private final ItemQueryService itemQueryService;

    @GetMapping
    @Operation(summary = "품목 검색", description = "품목을 검색한다.")
    public ResponseEntity<List<ItemDTO>> readItemList(ItemFilterRequest itemFilterRequest) {
        List<ItemDTO> itemDTO = itemQueryService.readItemList(itemFilterRequest);
        return ResponseEntity.ok(itemDTO);
    }
}

