package com.igar15.rest_course;

import com.igar15.rest_course.entity.AuthorityEntity;
import com.igar15.rest_course.entity.RoleEntity;
import com.igar15.rest_course.entity.UserEntity;
import com.igar15.rest_course.repository.AuthorityRepository;
import com.igar15.rest_course.repository.RoleRepository;
import com.igar15.rest_course.repository.UserRepository;
import com.igar15.rest_course.shared.Roles;
import com.igar15.rest_course.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Component
public class InitialUsersSetup {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    @EventListener
    @Transactional // do all or do nothing
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From application ready event ...");
        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        RoleEntity roleUser = createRole(Roles.ROLE_USER.name(), List.of(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), List.of(readAuthority, writeAuthority, deleteAuthority));

        if (roleAdmin == null) {
            return;
        }

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("Vasya");
        adminUser.setLastName("Kuralesov");
        adminUser.setEmail("vasya@test.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
        adminUser.setRoles(List.of(roleAdmin));

        userRepository.save(adminUser);
    }

    @Transactional
    private AuthorityEntity createAuthority(String name) {
        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
