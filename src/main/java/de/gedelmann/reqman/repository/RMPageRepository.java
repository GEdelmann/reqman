package de.gedelmann.reqman.repository;

import de.gedelmann.reqman.domain.RMPage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RMPage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RMPageRepository extends JpaRepository<RMPage, Long> {

}
