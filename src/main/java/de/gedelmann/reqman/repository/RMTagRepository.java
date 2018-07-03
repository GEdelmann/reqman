package de.gedelmann.reqman.repository;

import de.gedelmann.reqman.domain.RMTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the RMTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RMTagRepository extends JpaRepository<RMTag, Long> {

    @Query(value = "select distinct rm_tag from RMTag rm_tag left join fetch rm_tag.names",
        countQuery = "select count(distinct rm_tag) from RMTag rm_tag")
    Page<RMTag> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct rm_tag from RMTag rm_tag left join fetch rm_tag.names")
    List<RMTag> findAllWithEagerRelationships();

    @Query("select rm_tag from RMTag rm_tag left join fetch rm_tag.names where rm_tag.id =:id")
    Optional<RMTag> findOneWithEagerRelationships(@Param("id") Long id);

}
