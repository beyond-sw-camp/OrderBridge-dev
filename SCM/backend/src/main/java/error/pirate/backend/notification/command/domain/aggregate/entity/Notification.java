package error.pirate.backend.notification.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationSeq;

    private Long notificationAnotherSeq;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String notificationTitle;

    private String notificationContent;

    private String notificationReadYn = "N";

    private String notificationSendUser;

    @CreatedDate
    private LocalDateTime notificationRegDate;


}
