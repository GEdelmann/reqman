package de.gedelmann.reqman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RMPage.
 */
@Entity
@Table(name = "rm_page")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rmpage")
public class RMPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "rMPage")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RMAttachement> attachements = new HashSet<>();

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

    public RMPage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public RMPage description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RMAttachement> getAttachements() {
        return attachements;
    }

    public RMPage attachements(Set<RMAttachement> rMAttachements) {
        this.attachements = rMAttachements;
        return this;
    }

    public RMPage addAttachements(RMAttachement rMAttachement) {
        this.attachements.add(rMAttachement);
        rMAttachement.setRMPage(this);
        return this;
    }

    public RMPage removeAttachements(RMAttachement rMAttachement) {
        this.attachements.remove(rMAttachement);
        rMAttachement.setRMPage(null);
        return this;
    }

    public void setAttachements(Set<RMAttachement> rMAttachements) {
        this.attachements = rMAttachements;
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
        RMPage rMPage = (RMPage) o;
        if (rMPage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMPage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMPage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
