package de.gedelmann.reqman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RMCategory.
 */
@Entity
@Table(name = "rm_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rmcategory")
public class RMCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "rmcategory_name",
               joinColumns = @JoinColumn(name = "rmcategories_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "names_id", referencedColumnName = "id"))
    private Set<RMRequirement> names = new HashSet<>();

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

    public RMCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RMRequirement> getNames() {
        return names;
    }

    public RMCategory names(Set<RMRequirement> rMRequirements) {
        this.names = rMRequirements;
        return this;
    }

    public RMCategory addName(RMRequirement rMRequirement) {
        this.names.add(rMRequirement);
        rMRequirement.getCategories().add(this);
        return this;
    }

    public RMCategory removeName(RMRequirement rMRequirement) {
        this.names.remove(rMRequirement);
        rMRequirement.getCategories().remove(this);
        return this;
    }

    public void setNames(Set<RMRequirement> rMRequirements) {
        this.names = rMRequirements;
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
        RMCategory rMCategory = (RMCategory) o;
        if (rMCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
