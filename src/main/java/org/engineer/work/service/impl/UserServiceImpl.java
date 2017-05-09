package org.engineer.work.service.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.model.UserEntity;
import org.engineer.work.repository.UserRepository;
import org.engineer.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Default implementation of UserService.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity getUserByUsername(final String username) {
        UserEntity userEntity = null;
        if (username != null) {
            userEntity = userRepository.findOne(username);
        }
        return userEntity;
    }

    @Override
    @Transactional
    public boolean createUser(final UserDTO userDTO) {
        boolean result = false;
        if (userDTO != null && !userRepository.exists(userDTO.getUsername())) {
            userRepository.save(new UserEntity(userDTO));
            result = true;
        }
        return result;
    }

    @Override
    @Transactional
    public boolean updateUser(final UserEntity userEntity) {
        boolean result = false;
        if (userEntity != null && userRepository.exists(userEntity.getUsername())) {
            userRepository.save(userEntity);
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
    @Transactional
    public boolean deleteUser(final String username) {
        boolean result = false;
        if (username != null && userRepository.exists(username)) {
            userRepository.delete(username);
            result = true;
        }
        return result;
    }
}
