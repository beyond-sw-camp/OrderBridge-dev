package error.pirate.backend.client.command.application.dto;

import error.pirate.backend.user.command.domain.aggregate.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {
    private Long clientSeq;
    private User user;
    private String clientRegistrationNo;
    private String clientName;
    private String clientPhoneNo;
    private String clientEmail;
}
