/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.jooq.tables;

import edu.java.scrapper.domain.jooq.Public;
import edu.java.scrapper.domain.jooq.tables.records.DatabasechangelogRecord;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

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
public class Databasechangelog extends TableImpl<DatabasechangelogRecord> {

    /**
     * The reference instance of <code>public.databasechangelog</code>
     */
    public static final Databasechangelog DATABASECHANGELOG = new Databasechangelog();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>public.databasechangelog.id</code>.
     */
    public final TableField<DatabasechangelogRecord, String> ID =
        createField(DSL.name("id"), SQLDataType.VARCHAR(255).nullable(false), this, "");
    /**
     * The column <code>public.databasechangelog.author</code>.
     */
    public final TableField<DatabasechangelogRecord, String> AUTHOR =
        createField(DSL.name("author"), SQLDataType.VARCHAR(255).nullable(false), this, "");
    /**
     * The column <code>public.databasechangelog.filename</code>.
     */
    public final TableField<DatabasechangelogRecord, String> FILENAME =
        createField(DSL.name("filename"), SQLDataType.VARCHAR(255).nullable(false), this, "");
    /**
     * The column <code>public.databasechangelog.dateexecuted</code>.
     */
    public final TableField<DatabasechangelogRecord, LocalDateTime> DATEEXECUTED =
        createField(DSL.name("dateexecuted"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "");
    /**
     * The column <code>public.databasechangelog.orderexecuted</code>.
     */
    public final TableField<DatabasechangelogRecord, Integer> ORDEREXECUTED =
        createField(DSL.name("orderexecuted"), SQLDataType.INTEGER.nullable(false), this, "");
    /**
     * The column <code>public.databasechangelog.exectype</code>.
     */
    public final TableField<DatabasechangelogRecord, String> EXECTYPE =
        createField(DSL.name("exectype"), SQLDataType.VARCHAR(10).nullable(false), this, "");
    /**
     * The column <code>public.databasechangelog.md5sum</code>.
     */
    public final TableField<DatabasechangelogRecord, String> MD5SUM =
        createField(DSL.name("md5sum"), SQLDataType.VARCHAR(35), this, "");
    /**
     * The column <code>public.databasechangelog.description</code>.
     */
    public final TableField<DatabasechangelogRecord, String> DESCRIPTION =
        createField(DSL.name("description"), SQLDataType.VARCHAR(255), this, "");
    /**
     * The column <code>public.databasechangelog.comments</code>.
     */
    public final TableField<DatabasechangelogRecord, String> COMMENTS =
        createField(DSL.name("comments"), SQLDataType.VARCHAR(255), this, "");
    /**
     * The column <code>public.databasechangelog.tag</code>.
     */
    public final TableField<DatabasechangelogRecord, String> TAG =
        createField(DSL.name("tag"), SQLDataType.VARCHAR(255), this, "");
    /**
     * The column <code>public.databasechangelog.liquibase</code>.
     */
    public final TableField<DatabasechangelogRecord, String> LIQUIBASE =
        createField(DSL.name("liquibase"), SQLDataType.VARCHAR(20), this, "");
    /**
     * The column <code>public.databasechangelog.contexts</code>.
     */
    public final TableField<DatabasechangelogRecord, String> CONTEXTS =
        createField(DSL.name("contexts"), SQLDataType.VARCHAR(255), this, "");
    /**
     * The column <code>public.databasechangelog.labels</code>.
     */
    public final TableField<DatabasechangelogRecord, String> LABELS =
        createField(DSL.name("labels"), SQLDataType.VARCHAR(255), this, "");
    /**
     * The column <code>public.databasechangelog.deployment_id</code>.
     */
    public final TableField<DatabasechangelogRecord, String> DEPLOYMENT_ID =
        createField(DSL.name("deployment_id"), SQLDataType.VARCHAR(10), this, "");

    private Databasechangelog(Name alias, Table<DatabasechangelogRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Databasechangelog(
        Name alias,
        Table<DatabasechangelogRecord> aliased,
        Field<?>[] parameters,
        Condition where
    ) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.databasechangelog</code> table reference
     */
    public Databasechangelog(String alias) {
        this(DSL.name(alias), DATABASECHANGELOG);
    }

    /**
     * Create an aliased <code>public.databasechangelog</code> table reference
     */
    public Databasechangelog(Name alias) {
        this(alias, DATABASECHANGELOG);
    }

    /**
     * Create a <code>public.databasechangelog</code> table reference
     */
    public Databasechangelog() {
        this(DSL.name("databasechangelog"), null);
    }

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<DatabasechangelogRecord> getRecordType() {
        return DatabasechangelogRecord.class;
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @NotNull
    public Databasechangelog as(String alias) {
        return new Databasechangelog(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Databasechangelog as(Name alias) {
        return new Databasechangelog(alias, this);
    }

    @Override
    @NotNull
    public Databasechangelog as(Table<?> alias) {
        return new Databasechangelog(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Databasechangelog rename(String name) {
        return new Databasechangelog(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Databasechangelog rename(Name name) {
        return new Databasechangelog(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Databasechangelog rename(Table<?> name) {
        return new Databasechangelog(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public Databasechangelog where(Condition condition) {
        return new Databasechangelog(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public Databasechangelog where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public Databasechangelog where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public Databasechangelog where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public Databasechangelog where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public Databasechangelog where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public Databasechangelog where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public Databasechangelog where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public Databasechangelog whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public Databasechangelog whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}