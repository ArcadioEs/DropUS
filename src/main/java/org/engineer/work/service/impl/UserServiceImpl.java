package org.engineer.work.service.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;
import org.engineer.work.model.UserRole;
import org.engineer.work.model.enumeration.AuthorityRoles;
import org.engineer.work.repository.UserRepository;
import org.engineer.work.repository.UserRoleRepository;
import org.engineer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Default implementation of {@link UserService} interface.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserEntity getUserByUsername(final String username) {
        return userRepository.findOne(username);
    }

    @Override
    @Transactional
    public boolean saveUser(final UserDTO userDTO, final boolean isAdmin) {
        boolean result = false;
        final String username = userDTO.getUsername();

        if (! userRepository.exists(username)) {
            userRepository.save(new UserEntity(userDTO));

            if (isAdmin) {
                userRoleRepository.save(new UserRole(username, AuthorityRoles.ADMIN));
            } else {
                userRoleRepository.save(new UserRole(username, AuthorityRoles.USER));
            }
            result = true;
        }
        return result;
    }

    // TODO: Update method!!!

    @Override
    public List<UserEntity> getAllUsers() {
        final List<UserEntity> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }


    @Override
    @Transactional
    public boolean deleteUser(final String username) {
        boolean result = false;

        if (userRepository.exists(username)) {
            userRepository.delete(username);
            result = true;
        }
        return result;
    }
}
