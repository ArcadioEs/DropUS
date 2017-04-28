package org.engineer.work.repository;

import org.engineer.work.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository to manage {@link UserEntity}.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
	/**
	 * Gets specific user for given username.
	 */
	UserEntity findByUsername(String username);
}
