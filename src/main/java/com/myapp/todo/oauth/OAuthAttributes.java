package com.myapp.todo.oauth;

import com.myapp.todo.entity.user.UserRole;
import com.myapp.todo.entity.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;
    private final String provider;
    private final String providerId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String provider, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .provider(registrationId)
                .providerId(String.valueOf(attributes.get("id")))
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    public UserEntity toEntity() {
        return UserEntity.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .userRole(UserRole.GUEST)
                .provider(provider)
                .providerId(providerId)
                .build();
    }
}
