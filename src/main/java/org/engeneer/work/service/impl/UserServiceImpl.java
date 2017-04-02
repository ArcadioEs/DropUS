package org.engeneer.work.service.impl;

import org.engeneer.work.model.UserEntity;
import org.engeneer.work.repository.UserRepository;
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
    UserRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByUsername(final String username) {
        return repository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity saveUser(final String username) {
        return repository.save(new UserEntity(username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAllUsers() {
        final List<UserEntity> list = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUser(final String username) {
        UserEntity user = repository.findByUsername(username);

        if (user != null) {
            repository.delete(user);
            return true;
        }
        return false;
    }
}
