package error.pirate.backend.client.query.controller;

import error.pirate.backend.client.query.dto.ClientFilterRequest;
import error.pirate.backend.client.query.dto.ClientListResponse;
import error.pirate.backend.client.query.dto.ClientResponse;
import error.pirate.backend.client.query.service.ClientQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Tag(name = "Client", description = "거래처 관리")
public class ClientQueryController {

    private final ClientQueryService clientQueryService;

    @GetMapping
    @Operation(summary = "거래처 목록 조회", description = "거래처 목록을 조회합니다.")
    public ResponseEntity<ClientListResponse> readClientList(ClientFilterRequest clientFilterRequest) {
        ClientListResponse response = clientQueryService.readClientList(clientFilterRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{clientSeq}")
    @Operation(summary = "거래처 상세 조회", description = "거래처 정보를 조회합니다.")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long clientSeq) {
        return ResponseEntity.ok(clientQueryService.getClient(clientSeq));
    }

    @GetMapping("/hint")
    @Operation(summary = "거래처 힌트 조회", description = "거래처 힌트를 조회합니다.")
    public ResponseEntity<List<String>> readClientHint(
            @RequestParam String keyword) {
        return ResponseEntity.ok(clientQueryService.readClientHint(keyword));
    }
}
