package de.gedelmann.reqman.repository;

import de.gedelmann.reqman.domain.RMRequirement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RMRequirement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RMRequirementRepository extends JpaRepository<RMRequirement, Long> {

}
