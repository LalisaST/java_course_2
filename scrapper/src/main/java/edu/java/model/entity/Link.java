package edu.java.model.entity;

import edu.java.model.scheme.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private URI url;

    @Column
    private OffsetDateTime lastUpdate;

    @Column
    private OffsetDateTime lastCheck;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Column
    private Integer commitCount;

    @Column
    private Integer answerCount;

    @Column
    private Integer commentCount;

    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats = new HashSet<>();
}
