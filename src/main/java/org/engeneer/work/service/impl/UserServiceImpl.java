package org.engeneer.work.service.impl;

import org.engeneer.work.model.UserEntity;
import org.engeneer.work.model.UserRole;
import org.engeneer.work.model.enumeration.AuthorityRoles;
import org.engeneer.work.repository.UserRepository;
import org.engeneer.work.repository.UserRoleRepository;
import org.engeneer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUser(final String username, final String password, boolean isAdmin) {
        userRepository.save(new UserEntity(username, password));
        userRoleRepository.save(new UserRole(username, AuthorityRoles.USER));
        if (isAdmin) {
            userRoleRepository.save(new UserRole(username, AuthorityRoles.ADMIN));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAllUsers() {
        final List<UserEntity> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public boolean deleteUser(final Long userId) {
//        final UserEntity user = userRepository.findById(userId);
//
//        if (user != null) {
//            userRepository.delete(user);
//            return true;
//        }
//        return false;
//    }
}
