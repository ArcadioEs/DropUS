package org.engineer.work.repository.bounding;

import org.engineer.work.model.bounding.UserGroups;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage {@link UserGroups}.
 */
@Repository
public interface UserGroupsRepository extends CrudRepository<UserGroups, String> {
}
