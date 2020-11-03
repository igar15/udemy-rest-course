package com.igar15.rest_course.entity;

import javax.persistence.*;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(name = "users_id")
    private UserEntity userEntity;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
