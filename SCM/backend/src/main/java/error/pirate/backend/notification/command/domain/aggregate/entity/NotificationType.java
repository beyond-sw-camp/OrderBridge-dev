package error.pirate.backend.notification.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    // 각자 컨트롤러의 mapping url 입력해야함!! 프론트에서 통신할 때 `/notificationType` 으로 호출할거임
    // value 값은 알림 메세지에서 사용할거니까 결재서류명 입력해야함

    purchase("구매서"),
    purchaseOrder("발주서"),
    shippingInstruction("출하지시서"),
    shippingSlip("출하전표");

    private final String value;

}
