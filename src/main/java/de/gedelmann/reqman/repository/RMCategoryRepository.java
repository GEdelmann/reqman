package de.gedelmann.reqman.repository;

import de.gedelmann.reqman.domain.RMCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the RMCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RMCategoryRepository extends JpaRepository<RMCategory, Long> {

    @Query(value = "select distinct rm_category from RMCategory rm_category left join fetch rm_category.names",
        countQuery = "select count(distinct rm_category) from RMCategory rm_category")
    Page<RMCategory> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct rm_category from RMCategory rm_category left join fetch rm_category.names")
    List<RMCategory> findAllWithEagerRelationships();

    @Query("select rm_category from RMCategory rm_category left join fetch rm_category.names where rm_category.id =:id")
    Optional<RMCategory> findOneWithEagerRelationships(@Param("id") Long id);

}
