package error.pirate.backend.user.command.application.dto.oauth2;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public abstract class OAuth2UserInfo {

    protected final Map<String, Object> attributes;

    public abstract String getId();
    public abstract String getUsername();
    public abstract String getUserEmail();
    public abstract String getUserProfileImgUrl();
    public abstract String getUserPhoneNo();
}
