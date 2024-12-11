package error.pirate.backend.item.command.application.controller;

import error.pirate.backend.item.command.application.dto.ItemCreateRequest;
import error.pirate.backend.item.command.application.dto.ItemDTO;
import error.pirate.backend.item.command.application.dto.ItemUpdateRequest;
import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
@Tag(name = "품목 관리", description = "품목 관리 API")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "품목 등록", description = "품목을 등록한다.")
    public ResponseEntity<String> createItem(@Valid @RequestBody ItemCreateRequest request) {
        itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{itemSeq}")
    @Operation(summary = "품목 수정", description =  "품목을 수정한다.")
    public ResponseEntity<Void> updateItem(@PathVariable Long itemSeq, @RequestBody ItemUpdateRequest request) {
        itemService.updateItem(itemSeq, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{itemSeq}")
    @Operation(summary = "품목 삭제", description = "품목의 상태를 삭제로 변경합니다.")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemSeq) {
        itemService.deleteItem(itemSeq);
        return ResponseEntity.noContent().build();
    }
}
