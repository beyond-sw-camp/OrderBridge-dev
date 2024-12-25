package error.pirate.backend.client.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClientResponse {
    private Long clientSeq;
    private String clientName;
    private String clientPhoneNo;
    private String clientEmail;
    private String clientRegistrationNo;
    private String clientRepresentative;
    private LocalDateTime clientRegDate;
    private LocalDateTime clientModDate;
}
