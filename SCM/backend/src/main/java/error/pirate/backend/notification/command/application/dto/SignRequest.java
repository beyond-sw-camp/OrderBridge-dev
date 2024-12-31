package error.pirate.backend.notification.command.application.dto;

import lombok.Data;

@Data
public class SignRequest {

    private Long notificationSeq;

    private String notificationImageUrl;

}
