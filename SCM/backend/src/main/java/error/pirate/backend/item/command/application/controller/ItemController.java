package error.pirate.backend.item.command.application.controller;

import error.pirate.backend.item.command.application.dto.ItemDTO;
import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Tag(name = "품목 관리", description = "품목 관리 API")
@Slf4j
public class ItemController {

    private final ItemService itemService;


    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        log.info("품목 등록: {}", itemDTO);
        Item createdItem = itemService.createItem(itemDTO);
        return ResponseEntity.ok(createdItem);
    }
}
