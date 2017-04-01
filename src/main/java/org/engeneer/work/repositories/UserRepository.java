package org.engeneer.work.repositories;

import org.engeneer.work.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 *
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	/**
	 * Gets specific user for given id.
	 */
	UserEntity findById(Long userId);

	/**
	 * Gets specific user for given username.
	 */
	UserEntity findByUsername(String username);
}
