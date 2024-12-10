package error.pirate.backend.client.command.application.dto;

import lombok.Data;

@Data
public class ClientUpdateRequest {
    private String clientName;
    private String clientPhoneNo;
    private String clientEmail;
    private String clientRegistrationNo;

    public ClientUpdateRequest(String s, String s1, String mail, String s2) {
    }
}
