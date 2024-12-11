package error.pirate.backend.client.query.service;

import error.pirate.backend.client.query.dto.ClientFilterRequest;
import error.pirate.backend.client.query.dto.ClientResponse;
import error.pirate.backend.client.query.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientQueryService {

    private final ClientMapper clientMapper;

    public List<ClientResponse> readClientList(ClientFilterRequest filterRequest) {
        return clientMapper.findClientListByFilter(
                filterRequest.getClientName(),
                filterRequest.getClientRegistrationNo(),
                filterRequest.getSortBy(),
                filterRequest.getSortDirection(),
                0, // 페이지 시작
                10 // 페이지 크기
        );
    }
}
