package de.gedelmann.reqman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import de.gedelmann.reqman.domain.enumeration.RMRequirementScope;

import de.gedelmann.reqman.domain.enumeration.RMRequirementType;

/**
 * A RMRequirement.
 */
@Entity
@Table(name = "rm_requirement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rmrequirement")
public class RMRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "functional_id", nullable = false)
    private String functionalID;

    @Column(name = "headline")
    private String headline;

    @Column(name = "description")
    private String description;

    @Column(name = "general_note")
    private String generalNote;

    @Column(name = "technical_notes")
    private String technicalNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "_scope")
    private RMRequirementScope scope;

    @Enumerated(EnumType.STRING)
    @Column(name = "_type")
    private RMRequirementType type;

    @OneToMany(mappedBy = "rMRequirement")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RMAttachement> attachements = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("requirements")
    private RMProject project;

    @ManyToMany(mappedBy = "names")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RMTag> tags = new HashSet<>();

    @ManyToMany(mappedBy = "names")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RMCategory> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunctionalID() {
        return functionalID;
    }

    public RMRequirement functionalID(String functionalID) {
        this.functionalID = functionalID;
        return this;
    }

    public void setFunctionalID(String functionalID) {
        this.functionalID = functionalID;
    }

    public String getHeadline() {
        return headline;
    }

    public RMRequirement headline(String headline) {
        this.headline = headline;
        return this;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public RMRequirement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public RMRequirement generalNote(String generalNote) {
        this.generalNote = generalNote;
        return this;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public String getTechnicalNotes() {
        return technicalNotes;
    }

    public RMRequirement technicalNotes(String technicalNotes) {
        this.technicalNotes = technicalNotes;
        return this;
    }

    public void setTechnicalNotes(String technicalNotes) {
        this.technicalNotes = technicalNotes;
    }

    public RMRequirementScope getScope() {
        return scope;
    }

    public RMRequirement scope(RMRequirementScope scope) {
        this.scope = scope;
        return this;
    }

    public void setScope(RMRequirementScope scope) {
        this.scope = scope;
    }

    public RMRequirementType getType() {
        return type;
    }

    public RMRequirement type(RMRequirementType type) {
        this.type = type;
        return this;
    }

    public void setType(RMRequirementType type) {
        this.type = type;
    }

    public Set<RMAttachement> getAttachements() {
        return attachements;
    }

    public RMRequirement attachements(Set<RMAttachement> rMAttachements) {
        this.attachements = rMAttachements;
        return this;
    }

    public RMRequirement addAttachements(RMAttachement rMAttachement) {
        this.attachements.add(rMAttachement);
        rMAttachement.setRMRequirement(this);
        return this;
    }

    public RMRequirement removeAttachements(RMAttachement rMAttachement) {
        this.attachements.remove(rMAttachement);
        rMAttachement.setRMRequirement(null);
        return this;
    }

    public void setAttachements(Set<RMAttachement> rMAttachements) {
        this.attachements = rMAttachements;
    }

    public RMProject getProject() {
        return project;
    }

    public RMRequirement project(RMProject rMProject) {
        this.project = rMProject;
        return this;
    }

    public void setProject(RMProject rMProject) {
        this.project = rMProject;
    }

    public Set<RMTag> getTags() {
        return tags;
    }

    public RMRequirement tags(Set<RMTag> rMTags) {
        this.tags = rMTags;
        return this;
    }

    public RMRequirement addTags(RMTag rMTag) {
        this.tags.add(rMTag);
        rMTag.getNames().add(this);
        return this;
    }

    public RMRequirement removeTags(RMTag rMTag) {
        this.tags.remove(rMTag);
        rMTag.getNames().remove(this);
        return this;
    }

    public void setTags(Set<RMTag> rMTags) {
        this.tags = rMTags;
    }

    public Set<RMCategory> getCategories() {
        return categories;
    }

    public RMRequirement categories(Set<RMCategory> rMCategories) {
        this.categories = rMCategories;
        return this;
    }

    public RMRequirement addCategories(RMCategory rMCategory) {
        this.categories.add(rMCategory);
        rMCategory.getNames().add(this);
        return this;
    }

    public RMRequirement removeCategories(RMCategory rMCategory) {
        this.categories.remove(rMCategory);
        rMCategory.getNames().remove(this);
        return this;
    }

    public void setCategories(Set<RMCategory> rMCategories) {
        this.categories = rMCategories;
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
        RMRequirement rMRequirement = (RMRequirement) o;
        if (rMRequirement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMRequirement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMRequirement{" +
            "id=" + getId() +
            ", functionalID='" + getFunctionalID() + "'" +
            ", headline='" + getHeadline() + "'" +
            ", description='" + getDescription() + "'" +
            ", generalNote='" + getGeneralNote() + "'" +
            ", technicalNotes='" + getTechnicalNotes() + "'" +
            ", scope='" + getScope() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
