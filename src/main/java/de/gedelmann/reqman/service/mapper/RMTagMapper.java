package de.gedelmann.reqman.service.mapper;

import de.gedelmann.reqman.domain.*;
import de.gedelmann.reqman.service.dto.RMTagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RMTag and its DTO RMTagDTO.
 */
@Mapper(componentModel = "spring", uses = {RMRequirementMapper.class})
public interface RMTagMapper extends EntityMapper<RMTagDTO, RMTag> {



    default RMTag fromId(Long id) {
        if (id == null) {
            return null;
        }
        RMTag rMTag = new RMTag();
        rMTag.setId(id);
        return rMTag;
    }
}
