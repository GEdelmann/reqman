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
 * A RMProject.
 */
@Entity
@Table(name = "rm_project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rmproject")
public class RMProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RMRequirement> requirements = new HashSet<>();

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

    public RMProject name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RMRequirement> getRequirements() {
        return requirements;
    }

    public RMProject requirements(Set<RMRequirement> rMRequirements) {
        this.requirements = rMRequirements;
        return this;
    }

    public RMProject addRequirements(RMRequirement rMRequirement) {
        this.requirements.add(rMRequirement);
        rMRequirement.setProject(this);
        return this;
    }

    public RMProject removeRequirements(RMRequirement rMRequirement) {
        this.requirements.remove(rMRequirement);
        rMRequirement.setProject(null);
        return this;
    }

    public void setRequirements(Set<RMRequirement> rMRequirements) {
        this.requirements = rMRequirements;
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
        RMProject rMProject = (RMProject) o;
        if (rMProject.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMProject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMProject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
