package error.pirate.backend.notification.command.application.controller;

import error.pirate.backend.notification.command.domain.aggregate.entity.Notification;
import error.pirate.backend.notification.command.domain.service.NotificationDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Tag(name = "Notification", description = "알림")
public class NotificationController {

    private final NotificationDomainService notificationDomainService;

    @GetMapping
    @Operation(summary = "알림 조회")
    public ResponseEntity<List<Notification>> findAllBySendUserNameNot() {
        return ResponseEntity.ok(notificationDomainService.findAllBySendUserNameNot());
    }

}
