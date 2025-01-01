package error.pirate.backend.notification.command.domain.repository;

import error.pirate.backend.notification.command.domain.aggregate.entity.Notification;
import error.pirate.backend.notification.command.domain.aggregate.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.notificationSendUser != :userName ORDER BY n.notificationRegDate DESC")
    List<Notification> findAllBySendUserNameNot(String userName);

    @Query("SELECT n FROM Notification n WHERE n.notificationType = :notificationType AND n.notificationAnotherSeq = :notificationAnotherSeq ORDER BY n.notificationRegDate DESC")
   Notification findNotification(NotificationType notificationType, Long notificationAnotherSeq);

}
