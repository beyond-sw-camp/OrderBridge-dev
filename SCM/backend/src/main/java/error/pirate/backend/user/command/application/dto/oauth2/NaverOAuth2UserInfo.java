package error.pirate.backend.user.command.application.dto.oauth2;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) response(attributes).get("id");
    }

    @Override
    public String getUsername() {
        return (String) response(attributes).get("name");
    }

    @Override
    public String getUserEmail() {
        return (String) response(attributes).get("email");
    }

    @Override
    public String getUserProfileImgUrl() {
        return (String) response(attributes).get("profile_image");
    }

    @Override
    public String getUserPhoneNo() {
        return (String) response(attributes).get("phoneNo");
    }

    protected static Map<String, Object> response(Map<String, Object> attributes) {
        return (Map<String, Object>) attributes.get("response");
    }
}
