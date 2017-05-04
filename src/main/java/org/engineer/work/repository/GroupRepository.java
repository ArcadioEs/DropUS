package org.engineer.work.repository;

import org.engineer.work.model.GroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage {@link GroupEntity}.
 */
@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, String> {
}
