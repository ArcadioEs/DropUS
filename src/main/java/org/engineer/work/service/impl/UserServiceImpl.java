package org.engineer.work.service.impl;

import org.engineer.work.dto.UserDTO;
import org.engineer.work.exception.user.UserExistsException;
import org.engineer.work.exception.user.UserNotFoundException;
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
        return userRepository.findOne(username);
    }

    @Override
    @Transactional
    public void createUser(final UserDTO userDTO) throws UserExistsException {
        final String username = userDTO.getUsername();

        if (! userRepository.exists(username)) {
            userRepository.save(new UserEntity(userDTO));
        } else {
            throw new UserExistsException(username);
        }
    }

    @Override
    @Transactional
    public void updateUser(final UserEntity userEntity) throws UserNotFoundException {
        if (userRepository.exists(userEntity.getUsername())) {
            userRepository.save(userEntity);
        } else {
            throw new UserNotFoundException(userEntity.getUsername());
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        final List<UserEntity> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }


    @Override
    @Transactional
    public void deleteUser(final String username) throws UserNotFoundException {
        if (userRepository.exists(username)) {
            userRepository.delete(username);
        } else {
            throw new UserNotFoundException(username);
        }
    }
}
