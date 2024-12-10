package error.pirate.backend.client.command.application.controller;

import error.pirate.backend.client.command.application.dto.ClientCreateRequest;
import error.pirate.backend.client.command.application.dto.ClientUpdateRequest;
import error.pirate.backend.client.command.application.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
@Tag(name = "거래처 관리", description = "거래처 관리 API")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Void> createClient(@Valid @RequestBody ClientCreateRequest request) {
        clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/{clientSeq}")
    @Operation(summary = "거래처 수정", description = "거래처 정보를 수정합니다.")
    public ResponseEntity<Void> updateClient(@PathVariable Long clientSeq, @RequestBody ClientUpdateRequest request) {
        clientService.updateClient(clientSeq, request);
        return ResponseEntity.ok().build();
    }
}
