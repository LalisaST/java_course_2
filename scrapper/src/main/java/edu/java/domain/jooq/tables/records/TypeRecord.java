/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.records;

import edu.java.domain.jooq.tables.Type;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

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
public class TypeRecord extends UpdatableRecordImpl<TypeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.type.type</code>.
     */
    public void setType(@NotNull String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.type.type</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 63)
    @NotNull
    public String getType() {
        return (String) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TypeRecord
     */
    public TypeRecord() {
        super(Type.TYPE);
    }

    /**
     * Create a detached, initialised TypeRecord
     */
    @ConstructorProperties({"type"})
    public TypeRecord(@NotNull String type) {
        super(Type.TYPE);

        setType(type);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised TypeRecord
     */
    public TypeRecord(edu.java.domain.jooq.tables.pojos.Type value) {
        super(Type.TYPE);

        if (value != null) {
            setType(value.getType());
            resetChangedOnNotNull();
        }
    }
}