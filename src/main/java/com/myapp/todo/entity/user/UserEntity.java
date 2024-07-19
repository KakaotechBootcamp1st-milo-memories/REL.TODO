package com.myapp.todo.entity.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Column
    private String provider; //공급자 (google, facebook ...)

    @Column
    private String providerId; //공급 아이디

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;


    @Builder
    public UserEntity(String name, String email, String picture, UserRole userRole, String provider, String providerId, String accessToken) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.userRole = userRole;
        this.provider = provider;
        this.providerId = providerId;
    }

    public UserEntity update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.userRole.getKey();
    }

}