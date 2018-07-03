package de.gedelmann.reqman.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import de.gedelmann.reqman.domain.enumeration.RMTagType;

/**
 * A DTO for the RMTag entity.
 */
public class RMTagDTO implements Serializable {

    private Long id;

    private String name;

    private RMTagType type;

    private Set<RMRequirementDTO> names = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RMTagType getType() {
        return type;
    }

    public void setType(RMTagType type) {
        this.type = type;
    }

    public Set<RMRequirementDTO> getNames() {
        return names;
    }

    public void setNames(Set<RMRequirementDTO> rMRequirements) {
        this.names = rMRequirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RMTagDTO rMTagDTO = (RMTagDTO) o;
        if (rMTagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMTagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMTagDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
