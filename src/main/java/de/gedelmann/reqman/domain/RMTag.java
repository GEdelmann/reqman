package de.gedelmann.reqman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import de.gedelmann.reqman.domain.enumeration.RMTagType;

/**
 * A RMTag.
 */
@Entity
@Table(name = "rm_tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rmtag")
public class RMTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "_type")
    private RMTagType type;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "rmtag_name",
               joinColumns = @JoinColumn(name = "rmtags_id", referencedColumnName = "id"),
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

    public RMTag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RMTagType getType() {
        return type;
    }

    public RMTag type(RMTagType type) {
        this.type = type;
        return this;
    }

    public void setType(RMTagType type) {
        this.type = type;
    }

    public Set<RMRequirement> getNames() {
        return names;
    }

    public RMTag names(Set<RMRequirement> rMRequirements) {
        this.names = rMRequirements;
        return this;
    }

    public RMTag addName(RMRequirement rMRequirement) {
        this.names.add(rMRequirement);
        rMRequirement.getTags().add(this);
        return this;
    }

    public RMTag removeName(RMRequirement rMRequirement) {
        this.names.remove(rMRequirement);
        rMRequirement.getTags().remove(this);
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
        RMTag rMTag = (RMTag) o;
        if (rMTag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rMTag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RMTag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
