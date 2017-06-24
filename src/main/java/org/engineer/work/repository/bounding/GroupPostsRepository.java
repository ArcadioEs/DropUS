package org.engineer.work.repository.bounding;

import org.engineer.work.model.bounding.GroupPosts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage {@link GroupPosts}.
 */
@Repository
public interface GroupPostsRepository extends CrudRepository<GroupPosts, String> {
}
