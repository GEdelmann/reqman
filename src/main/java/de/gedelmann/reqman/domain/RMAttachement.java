package de.gedelmann.reqman.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RMAttachement.
 */
@Entity
@Table(name = "rm_attachement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rmattachement")
public class RMAttachement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mime_type")
    private String mimeType;

    @Lob
    @Column(name = "content")
    private byte[] content;

    @Column(name = "content_content_type")
    private String contentContentType;

    @ManyToOne
    @JsonIgnoreProperties("attachements")
    private RMRequirement rMRequirement;

    @ManyToOne
    @JsonIgnoreProperties("attachements")
    private RMPage rMPage;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RMAttachement name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public RMAttachement mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public RMAttachement content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public RMAttachement contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public RMRequirement getRMRequirement() {
        return rMRequirement;
    }

    public RMAttachement rMRequirement(RMRequirement rMRequirement) {
        this.rMRequirement = rMRequirement;
        return this;
    }

    public void setRMRequirement(RMRequirement rMRequirement) {
        this.rMRequirement = rMRequirement;
    }

    public RMPage getRMPage() {
        return rMPage;
    }

    public RMAttachement rMPage(RMPage rMPage) {
        this.rMPage = rMPage;
        return this;
    }

    public void setRMPage(RMPage rMPage) {
        this.rMPage = rMPage;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RMAttachement rMAttachement = (RMAttachement) o;
        if (rMAttachement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMAttachement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMAttachement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            "}";
    }
}
