package org.engeneer.work.repository;

import org.engeneer.work.model.UserEntity;
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
