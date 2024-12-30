package error.pirate.backend.item.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import error.pirate.backend.item.command.application.dto.ItemCreateRequest;
import error.pirate.backend.item.command.application.dto.ItemUpdateRequest;
import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.common.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    private final FileUploadUtil fileUploadUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "품목 등록", description = "파일과 함께 품목을 등록합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "multipart/form-data 형식의 요청 본문", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
    )
    )
    public ResponseEntity<String> createItem(
            @Parameter(description = "업로드할 파일", required = true)
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "품목 생성 요청 JSON ", required = true)
            @RequestPart("request") ItemCreateRequest request
    ) throws IOException {
        itemService.createItem(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{itemSeq}")
    @Operation(summary = "품목 수정", description = "품목 정보를 수정합니다.")
    public ResponseEntity<Void> updateItem(
            @PathVariable Long itemSeq,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "request") ItemUpdateRequest request) throws IOException {
        if (file != null && !file.isEmpty()) {
            String imageUrl = fileUploadUtil.uploadFile(file);
            request.setItemImageUrl(imageUrl);
        }
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
