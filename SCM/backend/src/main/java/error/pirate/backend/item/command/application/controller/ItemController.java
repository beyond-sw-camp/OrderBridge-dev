package error.pirate.backend.item.command.application.controller;

import error.pirate.backend.item.command.application.dto.ItemCreateRequest;
import error.pirate.backend.item.command.application.dto.ItemUpdateRequest;
import error.pirate.backend.item.command.application.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
@Tag(name = "품목 관리", description = "품목 관리 API")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "품목 등록", description = "파일과 함께 품목을 등록합니다.")
    public ResponseEntity<String> createItem(@RequestPart("file") MultipartFile file, @RequestPart("itemCreateRequest") ItemCreateRequest request) throws IOException {
        itemService.createItem(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{itemSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "품목 수정", description = "품목 정보를 수정합니다.")
    public ResponseEntity<Void> updateItem(@PathVariable Long itemSeq, @RequestPart("file") MultipartFile file, @RequestPart("itemUpdateRequest") ItemUpdateRequest request) throws IOException {

        itemService.updateItem(itemSeq, request, file);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{itemSeq}")
    @Operation(summary = "품목 삭제", description = "품목의 상태를 삭제로 변경합니다.")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemSeq) {
        itemService.deleteItem(itemSeq);
        return ResponseEntity.noContent().build();
    }

}
