package de.gedelmann.reqman.service.mapper;

import de.gedelmann.reqman.domain.*;
import de.gedelmann.reqman.service.dto.RMPageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RMPage and its DTO RMPageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RMPageMapper extends EntityMapper<RMPageDTO, RMPage> {


    @Mapping(target = "attachements", ignore = true)
    RMPage toEntity(RMPageDTO rMPageDTO);

    default RMPage fromId(Long id) {
        if (id == null) {
            return null;
        }
        RMPage rMPage = new RMPage();
        rMPage.setId(id);
        return rMPage;
    }
}
