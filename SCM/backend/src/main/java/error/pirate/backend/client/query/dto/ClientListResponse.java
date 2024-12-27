package error.pirate.backend.client.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientListResponse {
    private List<ClientResponse> clients;
    private int totalCount;
}