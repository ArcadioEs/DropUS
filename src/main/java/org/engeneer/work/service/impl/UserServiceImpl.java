package org.engeneer.work.service.impl;

import org.engeneer.work.model.UserEntity;
import org.engeneer.work.model.UserRole;
import org.engeneer.work.model.enumeration.AuthorityRoles;
import org.engeneer.work.repository.UserRepository;
import org.engeneer.work.repository.UserRoleRepository;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Default implementation of {@link UserService} interface.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserEntity getUserByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean saveUser(final String username, final String password, boolean isAdmin) {
        boolean result = false;
        if (getUserByUsername(username) == null) {
            userRepository.save(new UserEntity(username, passwordEncoder.encode(password)));

            userRoleRepository.save(new UserRole(username, AuthorityRoles.USER));
            if (isAdmin) {
                userRoleRepository.save(new UserRole(username, AuthorityRoles.ADMIN));
            }
            result = true;
        }
        return result;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        final List<UserEntity> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }


    @Override
    public boolean deleteUser(final String username) {
        final UserEntity user = userRepository.findByUsername(username);

        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}
