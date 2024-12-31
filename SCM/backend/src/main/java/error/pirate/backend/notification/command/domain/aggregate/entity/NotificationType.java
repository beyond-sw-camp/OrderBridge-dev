package error.pirate.backend.notification.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    PURCHASE("구매서"),
    PURCHASE_ORDER("발주서");

    private final String value;

}
