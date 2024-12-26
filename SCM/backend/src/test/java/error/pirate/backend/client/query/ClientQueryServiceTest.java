package error.pirate.backend.client.query;

import error.pirate.backend.client.query.dto.ClientFilterRequest;
import error.pirate.backend.client.query.dto.ClientResponse;
import error.pirate.backend.client.query.service.ClientQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@Transactional
class ClientQueryServiceTest {

    @Autowired
    private ClientQueryService clientQueryService;

    private static Stream<Arguments> clientFilterParams() {
        return Stream.of(
                arguments("항공", "123456789", "clientName", "ASC"),
                arguments("", "123456789", "clientSeq", "DESC"),
                arguments("항공", "", "clientName", "ASC"),
                arguments("", "", "clientSeq", "ASC")
        );
    }

    @DisplayName("거래처 목록 조회 테스트")
    @ParameterizedTest
    @MethodSource("clientFilterParams")
    void readClientListTest(String clientName, String clientRegistrationNo, String sortBy, String sortDirection) {
        ClientFilterRequest filterRequest = new ClientFilterRequest();
        filterRequest.setClientName(clientName);
        filterRequest.setClientRegistrationNo(clientRegistrationNo);

        List<ClientResponse> clientResponses = clientQueryService.readClientList(filterRequest).getClients();

        assertThat(clientResponses).isNotNull();
        assertThat(clientResponses).allMatch(client ->
                (clientName.isEmpty() || client.getClientName().contains(clientName)) &&
                        (clientRegistrationNo.isEmpty() || client.getClientRegistrationNo().contains(clientRegistrationNo))
        );
    }
}

