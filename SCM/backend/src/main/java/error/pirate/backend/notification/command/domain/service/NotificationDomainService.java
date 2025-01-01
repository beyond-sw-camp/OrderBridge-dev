package error.pirate.backend.notification.command.domain.service;

import error.pirate.backend.common.FileUploadUtil;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.notification.command.application.dto.NotificationRequest;
import error.pirate.backend.notification.command.application.dto.SignRequest;
import error.pirate.backend.notification.command.domain.aggregate.entity.Notification;
import error.pirate.backend.notification.command.domain.aggregate.entity.NotificationType;
import error.pirate.backend.notification.command.domain.repository.NotificationRepository;
import error.pirate.backend.user.command.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationDomainService {

    private final NotificationRepository notificationRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final FileUploadUtil fileUploadUtil;

    public Notification findById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodeType.COMMON_ERROR));
    }

    public List<Notification> findAllBySendUserNameNot() {
        return notificationRepository.findAllBySendUserNameNot(userService.findUserName());
    }

    public Notification createNotification(NotificationRequest request) {
        Notification notification = modelMapper.map(request, Notification.class);

        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createNotificationMessage(NotificationType type, Long seq, String fileName) {
        String title = "[결재 요청]";
        String content = "결재할 문서가 도착하였습니다. <br> " +
                type.getValue() + "명 : " + fileName;
        String sendUser = userService.findUserName();

        return createNotification(NotificationRequest.builder()
                .notificationAnotherSeq(seq)
                .notificationType(type)
                .notificationTitle(title)
                .notificationContent(content)
                .notificationSendUser(sendUser)
                .build());
    }

    @Transactional
    public void createSign(SignRequest signRequestDto) {
        String filePath = saveImageToFile(signRequestDto.getNotificationImageUrl());
        signRequestDto.setNotificationImageUrl(filePath);

        Notification notification = findById(signRequestDto.getNotificationSeq());
        notification.addNotificationSign(filePath, "Y");
    }

    private String saveImageToFile(String base64Image) {
        try {
            return fileUploadUtil.uploadImagePath(base64Image);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public Notification findNotification(NotificationType notificationType, Long notificationAnotherSeq) {
        return notificationRepository.findNotification(notificationType, notificationAnotherSeq);
    }

}

