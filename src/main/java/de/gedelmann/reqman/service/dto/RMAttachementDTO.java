package de.gedelmann.reqman.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the RMAttachement entity.
 */
public class RMAttachementDTO implements Serializable {

    private Long id;

    private String name;

    private String mimeType;

    @Lob
    private byte[] content;
    private String contentContentType;

    private Long rMRequirementId;

    private Long rMPageId;

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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Long getRMRequirementId() {
        return rMRequirementId;
    }

    public void setRMRequirementId(Long rMRequirementId) {
        this.rMRequirementId = rMRequirementId;
    }

    public Long getRMPageId() {
        return rMPageId;
    }

    public void setRMPageId(Long rMPageId) {
        this.rMPageId = rMPageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RMAttachementDTO rMAttachementDTO = (RMAttachementDTO) o;
        if (rMAttachementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMAttachementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMAttachementDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", content='" + getContent() + "'" +
            ", rMRequirement=" + getRMRequirementId() +
            ", rMPage=" + getRMPageId() +
            "}";
    }
}
