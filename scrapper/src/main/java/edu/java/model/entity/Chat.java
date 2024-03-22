package edu.java.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Chat {
    @Id
    private Long id;

    @Column
    private OffsetDateTime createAt;

    @ManyToMany
    private Set<Link> links = new HashSet<>();

    public void addLink(Link link) {
        links.add(link);
        link.getChats().add(this);
    }

    public void removeLink(Link link) {
        links.remove(link);
        link.getChats().remove(this);
    }
}
