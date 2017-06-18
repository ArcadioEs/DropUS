package org.engineer.work.repository;

import org.engineer.work.model.PostEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage {@link PostEntity}.
 */
@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long> {
}
