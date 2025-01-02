package error.pirate.backend.item.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import error.pirate.backend.item.command.application.dto.ItemCreateRequest;
import error.pirate.backend.item.command.application.dto.ItemUpdateRequest;
import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.common.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Getter
    private final FileUploadUtil fileUploadUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "품목 등록", description = "파일과 함께 품목을 등록합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "multipart/form-data 형식의 요청 본문", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
    )
    )
    public ResponseEntity<String> createItem(
            @Parameter(description = "업로드할 파일", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "품목 생성 요청 JSON (String 형태)", required = true)
            @RequestParam("request") String requestStr
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ItemCreateRequest request = mapper.readValue(requestStr, ItemCreateRequest.class);
        itemService.createItem(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{itemSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("permitAll()")
    @Operation(summary = "품목 수정", description = "품목 정보를 수정합니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "multipart/form-data 형식의 요청 본문", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
    ))
    public ResponseEntity<Void> updateItem(
            @PathVariable Long itemSeq,
            @Parameter(description = "업로드할 파일", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "품목 수정 요청 JSON (String 형태)", required = true)
            @RequestParam("request") String requestStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ItemUpdateRequest request = mapper.readValue(requestStr, ItemUpdateRequest.class);
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
