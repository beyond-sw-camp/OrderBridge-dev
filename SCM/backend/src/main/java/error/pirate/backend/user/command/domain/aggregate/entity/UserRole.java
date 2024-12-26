package error.pirate.backend.user.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    GUEST("ROLE_GUEST"),
    MEMBER("ROLE_MEMBER");

    private final String value;
}
