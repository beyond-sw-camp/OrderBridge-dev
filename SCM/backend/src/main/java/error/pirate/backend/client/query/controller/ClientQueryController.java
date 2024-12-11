package error.pirate.backend.client.query.controller;

import error.pirate.backend.client.query.dto.ClientFilterRequest;
import error.pirate.backend.client.query.dto.ClientResponse;
import error.pirate.backend.client.query.service.ClientQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Tag(name = "거래처 관리", description = "거래처 목록 조회 API")
public class ClientQueryController {

    private final ClientQueryService clientQueryService;

    @GetMapping
    @Operation(summary = "거래처 목록 조회", description = "거래처 목록을 필터 조건 및 정렬 기준에 따라 조회합니다.")
    public ResponseEntity<List<ClientResponse>> readClientList(ClientFilterRequest filterRequest) {
        List<ClientResponse> clientList = clientQueryService.readClientList(filterRequest);
        return ResponseEntity.ok(clientList);
    }
}
