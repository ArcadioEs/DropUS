package org.engeneer.work.repository;

import org.engeneer.work.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository to retrieve {@link UserEntity}.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	/**
	 * Gets specific user for given username.
	 */
	UserEntity findByUsername(String username);
}
