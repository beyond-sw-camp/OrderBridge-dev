package error.pirate.backend.client.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientFilterRequest {
    private String clientName;
    private String clientRegistrationNo;
    private int page = 1;
    private int size = 10;
}
