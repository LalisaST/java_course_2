package edu.java.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Setter
@Getter
public class Chat {
    @Id
    private Long id;

    @ColumnDefault("current_timestamp")
    @Column(insertable = false, updatable = false)
    private OffsetDateTime createAt;

    @ManyToMany
    @JoinTable(
        name = "chat_link",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
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
