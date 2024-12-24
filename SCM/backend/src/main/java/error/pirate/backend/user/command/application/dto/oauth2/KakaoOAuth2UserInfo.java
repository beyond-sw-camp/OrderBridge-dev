package error.pirate.backend.user.command.application.dto.oauth2;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }


    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getUsername() {
        return (String) accounts(attributes).get("name");
    }

    @Override
    public String getUserEmail() {
        return (String) accounts(attributes).get("email");
    }

    @Override
    public String getUserProfileImgUrl() {
        return (String) profiles(accounts(attributes)).get("profile_image_url");
    }

    @Override
    public String getUserPhoneNo() {
        return (String) accounts(attributes).get("phone_number");
    }

    protected static Map<String, Object> accounts(Map<String, Object> attributes) {
        return (Map<String, Object>) attributes.get("kakao_account");
    }

    protected static Map<String, Object> profiles(Map<String, Object> accounts) {
        return (Map<String, Object>) accounts.get("profile");
    }
}
