package error.pirate.backend.client.command.application.dto;

import lombok.Data;

@Data
public class ClientUpdateRequest {
    private String clientName;
    private String clientPhoneNo;
    private String clientEmail;
    private String clientRegistrationNo;
    private String clientRepresentative;

    public ClientUpdateRequest(String clientName, String clientPhoneNo, String clientEmail,
                               String clientRegistrationNo, String clientRepresentative) {
        this.clientName = clientName;
        this.clientPhoneNo = clientPhoneNo;
        this.clientEmail = clientEmail;
        this.clientRegistrationNo = clientRegistrationNo;
        this.clientRepresentative = clientRepresentative;
    }
}




