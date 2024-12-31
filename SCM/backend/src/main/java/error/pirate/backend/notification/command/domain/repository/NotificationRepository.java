package error.pirate.backend.notification.command.domain.repository;

import error.pirate.backend.notification.command.domain.aggregate.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.notificationSendUser != :userName")
    List<Notification> findAllBySendUserNameNot(String userName);

}
