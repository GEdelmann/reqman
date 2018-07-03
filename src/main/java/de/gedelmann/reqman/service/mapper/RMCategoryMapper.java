package de.gedelmann.reqman.service.mapper;

import de.gedelmann.reqman.domain.*;
import de.gedelmann.reqman.service.dto.RMCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RMCategory and its DTO RMCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {RMRequirementMapper.class})
public interface RMCategoryMapper extends EntityMapper<RMCategoryDTO, RMCategory> {



    default RMCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        RMCategory rMCategory = new RMCategory();
        rMCategory.setId(id);
        return rMCategory;
    }
}
