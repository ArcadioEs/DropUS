package org.engineer.work.repository;

import org.engineer.work.model.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage {@link UserRole}.
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

}
