package de.gedelmann.reqman.service.mapper;

import de.gedelmann.reqman.domain.*;
import de.gedelmann.reqman.service.dto.RMProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RMProject and its DTO RMProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RMProjectMapper extends EntityMapper<RMProjectDTO, RMProject> {


    @Mapping(target = "requirements", ignore = true)
    RMProject toEntity(RMProjectDTO rMProjectDTO);

    default RMProject fromId(Long id) {
        if (id == null) {
            return null;
        }
        RMProject rMProject = new RMProject();
        rMProject.setId(id);
        return rMProject;
    }
}
