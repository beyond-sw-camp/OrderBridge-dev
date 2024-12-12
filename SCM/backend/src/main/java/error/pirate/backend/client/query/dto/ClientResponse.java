package error.pirate.backend.client.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {
    private Long clientSeq;
    private String clientName;
    private String clientPhoneNo;
    private String clientEmail;
    private String clientRegistrationNo;
}
