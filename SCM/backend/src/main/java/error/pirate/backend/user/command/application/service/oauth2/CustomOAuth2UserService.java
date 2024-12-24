package error.pirate.backend.user.command.application.service.oauth2;

import error.pirate.backend.user.command.application.dto.oauth2.CustomOAuth2User;
import error.pirate.backend.user.command.application.dto.oauth2.OAuth2Attributes;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.aggregate.entity.UserSocialType;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        UserSocialType userSocialType = getUserSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // PK
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes extractAttributes = OAuth2Attributes.oAuthAttributes(userSocialType, userNameAttributeName, attributes);

        User user = userRepository.findByUserSocialTypeAndUserSocialId(userSocialType, extractAttributes.getOAuth2UserInfo().getId()).orElse(null);

        if(user == null) {
           user = userRepository.save(extractAttributes.socialUser(userSocialType, extractAttributes.getOAuth2UserInfo()));
        }

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().getValue())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                user.getUserEmployeeNo(),
                user.getUserRole()
        );
    }

    private UserSocialType getUserSocialType(String registrationId) {
        if(registrationId.equals("naver")) {
            return UserSocialType.NAVER;
        } else /*if(registrationId.equals("kakao"))*/ {
            return UserSocialType.KAKAO;
        }
    }
}
