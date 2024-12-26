package error.pirate.backend.client.query.service;

import error.pirate.backend.client.query.dto.ClientFilterRequest;
import error.pirate.backend.client.query.dto.ClientListResponse;
import error.pirate.backend.client.query.dto.ClientResponse;
import error.pirate.backend.client.query.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientQueryService {
    private final ClientMapper clientMapper;

    public ClientListResponse readClientList(ClientFilterRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();

        List<ClientResponse> clients = clientMapper.findClientListByFilter(
                request.getClientName(),
                request.getClientRegistrationNo(),
                offset,
                request.getSize()
        );

        int totalCount = clientMapper.countClientsByFilter(
                request.getClientName(),
                request.getClientRegistrationNo()
        );

        return new ClientListResponse(clients, totalCount);

    }
    public ClientResponse getClient(Long clientSeq) {
        return clientMapper.findClientSeq(clientSeq);
    }
}
