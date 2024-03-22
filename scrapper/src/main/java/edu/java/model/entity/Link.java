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
import org.hibernate.annotations.ColumnDefault;

@Entity
@Setter
@Getter
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private URI url;

    @ColumnDefault("current_timestamp")
    @Column(insertable = false)
    private OffsetDateTime lastUpdate;

    @ColumnDefault("current_timestamp")
    @Column(insertable = false)
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
