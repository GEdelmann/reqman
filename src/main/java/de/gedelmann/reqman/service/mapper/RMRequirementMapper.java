package de.gedelmann.reqman.service.mapper;

import de.gedelmann.reqman.domain.*;
import de.gedelmann.reqman.service.dto.RMRequirementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RMRequirement and its DTO RMRequirementDTO.
 */
@Mapper(componentModel = "spring", uses = {RMProjectMapper.class})
public interface RMRequirementMapper extends EntityMapper<RMRequirementDTO, RMRequirement> {

    @Mapping(source = "project.id", target = "projectId")
    RMRequirementDTO toDto(RMRequirement rMRequirement);

    @Mapping(target = "attachements", ignore = true)
    @Mapping(source = "projectId", target = "project")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "categories", ignore = true)
    RMRequirement toEntity(RMRequirementDTO rMRequirementDTO);

    default RMRequirement fromId(Long id) {
        if (id == null) {
            return null;
        }
        RMRequirement rMRequirement = new RMRequirement();
        rMRequirement.setId(id);
        return rMRequirement;
    }
}
