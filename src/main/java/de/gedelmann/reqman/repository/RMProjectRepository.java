package de.gedelmann.reqman.repository;

import de.gedelmann.reqman.domain.RMProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RMProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RMProjectRepository extends JpaRepository<RMProject, Long> {

}
