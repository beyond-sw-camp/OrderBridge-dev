package error.pirate.backend.client.command.application.service;

import error.pirate.backend.BackendApplication;
import error.pirate.backend.client.command.application.dto.ClientCreateRequest;
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
                arguments(new ClientCreateRequest(1L, "123-45-67890", "Test Client 1", "010-1234-5678", "client1@test.com")),
                arguments(new ClientCreateRequest(2L, "987-65-43210", "Test Client 2", "010-8765-4321", "client2@test.com")),
                arguments(new ClientCreateRequest(3L, "555-55-55555", "Test Client 3", "010-5555-5555", "client3@test.com"))
        );
    }

    @DisplayName("거래처 등록 테스트 - 성공")
    @ParameterizedTest
    @MethodSource("createClientArguments")
    void createClientTest(ClientCreateRequest request) {
        assertDoesNotThrow(() -> clientService.createClient(request));
    }
}

