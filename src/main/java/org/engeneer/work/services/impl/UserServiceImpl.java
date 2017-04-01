package org.engeneer.work.services.impl;

import org.engeneer.work.entities.UserEntity;
import org.engeneer.work.repositories.UserRepository;
import org.engeneer.work.services.UserService;
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
	public UserEntity getUserById(final Long id) {
		return repository.findById(id);
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
}
