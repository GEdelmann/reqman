package de.gedelmann.reqman.repository;

import de.gedelmann.reqman.domain.RMAttachement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RMAttachement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RMAttachementRepository extends JpaRepository<RMAttachement, Long> {

}
