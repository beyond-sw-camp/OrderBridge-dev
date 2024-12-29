package error.pirate.backend.notification.command.application.dto;

import error.pirate.backend.notification.command.domain.aggregate.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class NotificationRequest {

    private Long notificationAnotherSeq;

    private NotificationType notificationType;

    private String notificationTitle;

    private String notificationContent;

    private String notificationSendUser;

}
