package error.pirate.backend.client.command.application.service;

import error.pirate.backend.BackendApplication;
import error.pirate.backend.client.command.application.dto.ClientCreateRequest;
import error.pirate.backend.client.command.application.dto.ClientUpdateRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(classes = BackendApplication.class)
@Transactional
class ClientServiceTest {

    @Autowired
    private ClientService clientService;
    private static Stream<Arguments> createClientArguments() {
        return Stream.of(
                arguments(new ClientCreateRequest(
                        1L,                    // userSeq
                        "123-45-67890",       // clientRegistrationNo
                        "Test Client 1",       // clientName
                        "010-1234-5678",      // clientPhoneNo
                        "client1@test.com",    // clientEmail
                        "대표자1"              // clientRepresentative
                )),
                arguments(new ClientCreateRequest(
                        2L,
                        "987-65-43210",
                        "Test Client 2",
                        "010-8765-4321",
                        "client2@test.com",
                        "대표자2"
                )),
                arguments(new ClientCreateRequest(
                        3L,
                        "555-55-55555",
                        "Test Client 3",
                        "010-5555-5555",
                        "client3@test.com",
                        "대표자3"
                ))
        );
    }

    private static Stream<Arguments> updateClientArguments() {
        return Stream.of(
                arguments(1L, new ClientUpdateRequest(
                        "Updated Client 1",      // clientName
                        "010-1111-2222",        // clientPhoneNo
                        "updated1@test.com",     // clientEmail
                        "123-45-67890",         // clientRegistrationNo
                        "대표자1"                // clientRepresentative
                )),
                arguments(2L, new ClientUpdateRequest(
                        "Updated Client 2",
                        "010-2222-3333",
                        "updated2@test.com",
                        "987-65-43210",
                        "대표자2"
                )),
                arguments(3L, new ClientUpdateRequest(
                        "Updated Client 3",
                        "010-3333-4444",
                        "updated3@test.com",
                        "555-55-55555",
                        "대표자3"
                ))
        );
    }

    private static Stream<Arguments> deleteClientArguments() {
        return Stream.of(
                arguments(1L),
                arguments(2L),
                arguments(3L)
        );
    }

    @DisplayName("거래처 등록 테스트 - 성공")
    @ParameterizedTest
    @MethodSource("createClientArguments")
    void createClientTest(ClientCreateRequest request) {
        assertDoesNotThrow(() -> clientService.createClient(request));
    }

    @DisplayName("거래처 수정 테스트 - 성공")
    @ParameterizedTest
    @MethodSource("updateClientArguments")
    void updateClientTest(Long clientSeq, ClientUpdateRequest request) {
        assertDoesNotThrow(() -> clientService.updateClient(clientSeq, request));
    }

    @DisplayName("거래처 삭제 테스트 - 성공")
    @ParameterizedTest
    @MethodSource("deleteClientArguments")
    void deleteClientTest(Long clientSeq) {
        assertDoesNotThrow(() -> clientService.deleteClient(clientSeq));
    }
}
