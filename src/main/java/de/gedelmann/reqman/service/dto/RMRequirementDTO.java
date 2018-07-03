package de.gedelmann.reqman.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import de.gedelmann.reqman.domain.enumeration.RMRequirementScope;
import de.gedelmann.reqman.domain.enumeration.RMRequirementType;

/**
 * A DTO for the RMRequirement entity.
 */
public class RMRequirementDTO implements Serializable {

    private Long id;

    @NotNull
    private String functionalID;

    private String headline;

    private String description;

    private String generalNote;

    private String technicalNotes;

    private RMRequirementScope scope;

    private RMRequirementType type;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunctionalID() {
        return functionalID;
    }

    public void setFunctionalID(String functionalID) {
        this.functionalID = functionalID;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public String getTechnicalNotes() {
        return technicalNotes;
    }

    public void setTechnicalNotes(String technicalNotes) {
        this.technicalNotes = technicalNotes;
    }

    public RMRequirementScope getScope() {
        return scope;
    }

    public void setScope(RMRequirementScope scope) {
        this.scope = scope;
    }

    public RMRequirementType getType() {
        return type;
    }

    public void setType(RMRequirementType type) {
        this.type = type;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long rMProjectId) {
        this.projectId = rMProjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RMRequirementDTO rMRequirementDTO = (RMRequirementDTO) o;
        if (rMRequirementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMRequirementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMRequirementDTO{" +
            "id=" + getId() +
            ", functionalID='" + getFunctionalID() + "'" +
            ", headline='" + getHeadline() + "'" +
            ", description='" + getDescription() + "'" +
            ", generalNote='" + getGeneralNote() + "'" +
            ", technicalNotes='" + getTechnicalNotes() + "'" +
            ", scope='" + getScope() + "'" +
            ", type='" + getType() + "'" +
            ", project=" + getProjectId() +
            "}";
    }
}
