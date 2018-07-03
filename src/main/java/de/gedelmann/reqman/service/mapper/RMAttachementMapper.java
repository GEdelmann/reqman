package de.gedelmann.reqman.service.mapper;

import de.gedelmann.reqman.domain.*;
import de.gedelmann.reqman.service.dto.RMAttachementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RMAttachement and its DTO RMAttachementDTO.
 */
@Mapper(componentModel = "spring", uses = {RMRequirementMapper.class, RMPageMapper.class})
public interface RMAttachementMapper extends EntityMapper<RMAttachementDTO, RMAttachement> {

    @Mapping(source = "rMRequirement.id", target = "rMRequirementId")
    @Mapping(source = "rMPage.id", target = "rMPageId")
    RMAttachementDTO toDto(RMAttachement rMAttachement);

    @Mapping(source = "rMRequirementId", target = "rMRequirement")
    @Mapping(source = "rMPageId", target = "rMPage")
    RMAttachement toEntity(RMAttachementDTO rMAttachementDTO);

    default RMAttachement fromId(Long id) {
        if (id == null) {
            return null;
        }
        RMAttachement rMAttachement = new RMAttachement();
        rMAttachement.setId(id);
        return rMAttachement;
    }
}
