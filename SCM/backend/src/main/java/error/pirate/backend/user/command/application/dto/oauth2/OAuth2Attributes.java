package error.pirate.backend.user.command.application.dto.oauth2;

import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.aggregate.entity.UserRole;
import error.pirate.backend.user.command.domain.aggregate.entity.UserSocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class OAuth2Attributes {

    private final String nameAttributeKey; // OAuth2 로그인 시 키가 되는 필드 값
    private final OAuth2UserInfo oAuth2UserInfo;

    public static OAuth2Attributes oAuthAttributes(UserSocialType userSocialType, String userNameAttributeName, Map<String, Object> attributes) {
        if(userSocialType == UserSocialType.NAVER) {
            return naverAttributes(userNameAttributeName, attributes);
        } else /*if(userSocialType == UserSocialType.KAKAO)*/ {
            return kakaoAttributes(userNameAttributeName, attributes);
        }
    }

    private static OAuth2Attributes kakaoAttributes(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuth2Attributes(userNameAttributeName, new KakaoOAuth2UserInfo(attributes));
    }

    private static OAuth2Attributes naverAttributes(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuth2Attributes(userNameAttributeName, new NaverOAuth2UserInfo(attributes));
    }

    public User socialUser(UserSocialType userSocialType, OAuth2UserInfo oAuth2UserInfo) {
        String randomUserEmployeeNo = UUID.randomUUID().toString();
        String randomUserPwd = UUID.randomUUID().toString();

        return User.createUser(randomUserEmployeeNo, randomUserPwd, oAuth2UserInfo.getUserProfileImgUrl(),
                UserRole.GUEST, userSocialType, oAuth2UserInfo.getUsername(),
                oAuth2UserInfo.getUserPhoneNo(), oAuth2UserInfo.getUserEmail(), oAuth2UserInfo.getId());
    }
}
