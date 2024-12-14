package error.pirate.backend.client.command.application.service;

import error.pirate.backend.client.command.application.dto.ClientCreateRequest;
import error.pirate.backend.client.command.application.dto.ClientDTO;
import error.pirate.backend.client.command.application.dto.ClientUpdateRequest;
import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.client.command.domain.aggregate.repository.ClientRepository;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void createClient(ClientCreateRequest request) {
        User user = userRepository.findById(request.getUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));

        ClientDTO clientDTO = modelMapper.map(request, ClientDTO.class);
        clientDTO.setUser(user);

        Client client = modelMapper.map(clientDTO, Client.class);

        clientRepository.save(client);
    }

    @Transactional
    public void updateClient(Long clientSeq, ClientUpdateRequest request) {
        Client client = clientRepository.findById(clientSeq)
                .orElseThrow(() -> new IllegalArgumentException("Invalid clientSeq: " + clientSeq));


        client.updateClient(request);
    }
    @Transactional
    public void deleteClient(Long clientSeq) {
        Client client = clientRepository.findById(clientSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.CLIENT_STATUS_ERROR));

        // 상태를 DELETED로 변경
        client.delete();
    }
}

