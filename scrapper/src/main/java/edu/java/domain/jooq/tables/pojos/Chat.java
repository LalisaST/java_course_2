/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.pojos;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.19.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private OffsetDateTime createAt;

    public Chat() {
    }

    public Chat(Chat value) {
        this.id = value.id;
        this.createAt = value.createAt;
    }

    @ConstructorProperties({"id", "createAt"})
    public Chat(
        @NotNull Long id,
        @Nullable OffsetDateTime createAt
    ) {
        this.id = id;
        this.createAt = createAt;
    }

    /**
     * Getter for <code>public.chat.id</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.chat.id</code>.
     */
    public void setId(@NotNull Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>public.chat.create_at</code>.
     */
    @Nullable
    public OffsetDateTime getCreateAt() {
        return this.createAt;
    }

    /**
     * Setter for <code>public.chat.create_at</code>.
     */
    public void setCreateAt(@Nullable OffsetDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Chat other = (Chat) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.createAt == null) {
            if (other.createAt != null) {
                return false;
            }
        } else if (!this.createAt.equals(other.createAt)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.createAt == null) ? 0 : this.createAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Chat (");

        sb.append(id);
        sb.append(", ").append(createAt);

        sb.append(")");
        return sb.toString();
    }
}