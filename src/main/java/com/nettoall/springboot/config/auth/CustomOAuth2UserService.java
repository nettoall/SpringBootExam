package com.nettoall.springboot.config.auth;

import com.nettoall.springboot.config.auth.dto.OAuthAttributes;
import com.nettoall.springboot.config.auth.dto.SessionUser;
import com.nettoall.springboot.domain.user.User;
import com.nettoall.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User OAuth2User  = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인 진행중인 서비스 구분 코드
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // 키가 되는 필드값.

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, OAuth2User.getAttributes());
        User user = saveOrUpdate(attributes);
        // domain.user.User 클래스를 사용하지 않고 SessionUser를 별도로 정의하여 사용(User 클래스가 직렬화 되어 있지 않음.
        // domain.user.User 는 엔티티 클래스로 다른 엔티티와 관계가 형성될 수 있으므로 (DB 엔티티로만 사용하고 세션용은 따로  구현
        // 성능, 부수 효과 등의 이유로)
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
