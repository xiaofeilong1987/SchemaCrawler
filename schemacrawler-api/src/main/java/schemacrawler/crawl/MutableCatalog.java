/*
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package schemacrawler.crawl;


import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.ColumnDataType;
import schemacrawler.schema.CrawlHeaderInfo;
import schemacrawler.schema.DatabaseObject;
import schemacrawler.schema.NamedObject;
import schemacrawler.schema.Reducer;
import schemacrawler.schema.Reducible;
import schemacrawler.schema.Routine;
import schemacrawler.schema.Schema;
import schemacrawler.schema.SchemaReference;
import schemacrawler.schema.Sequence;
import schemacrawler.schema.Synonym;
import schemacrawler.schema.Table;

/**
 * Database and connection information. Created from metadata returned
 * by a JDBC call, and other sources of information.
 *
 * @author Sualeh Fatehi sualeh@hotmail.com
 */
final class MutableCatalog
  extends AbstractNamedObjectWithAttributes
  implements Catalog, Reducible
{

  private final class FilterBySchema
    implements Predicate<DatabaseObject>
  {

    private final Schema schema;

    public FilterBySchema(final Schema schema)
    {
      this.schema = requireNonNull(schema, "No schema provided");
    }

    @Override
    public boolean test(final DatabaseObject databaseObject)
    {
      return databaseObject != null
             && databaseObject.getSchema().equals(schema);
    }

  }

  private static final long serialVersionUID = 4051323422934251828L;
  private final MutableDatabaseInfo databaseInfo;
  private final MutableJdbcDriverInfo jdbcDriverInfo;
  private final ImmutableSchemaCrawlerInfo schemaCrawlerInfo;
  private ImmutableCrawlHeaderInfo crawlHeaderInfo;
  private final Set<Schema> schemas;
  private final ColumnDataTypes columnDataTypes = new ColumnDataTypes();
  private final NamedObjectList<MutableTable> tables = new NamedObjectList<>();
  private final NamedObjectList<MutableRoutine> routines = new NamedObjectList<>();
  private final NamedObjectList<MutableSynonym> synonyms = new NamedObjectList<>();

  private final NamedObjectList<MutableSequence> sequences = new NamedObjectList<>();

  MutableCatalog(final String name)
  {
    super(name);
    databaseInfo = new MutableDatabaseInfo();
    jdbcDriverInfo = new MutableJdbcDriverInfo();
    schemaCrawlerInfo = new ImmutableSchemaCrawlerInfo();
    schemas = new HashSet<>();
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSystemColumnDataTypes()
   */
  @Override
  public Collection<ColumnDataType> getColumnDataTypes()
  {
    return new ArrayList<ColumnDataType>(columnDataTypes.values());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSystemColumnDataTypes()
   */
  @Override
  public Collection<ColumnDataType> getColumnDataTypes(final Schema schema)
  {
    final FilterBySchema filter = new FilterBySchema(schema);
    return columnDataTypes.values().stream().filter(filter)
      .collect(Collectors.toList());
  }

  @Override
  public CrawlHeaderInfo getCrawlHeaderInfo()
  {
    return crawlHeaderInfo;
  }

  @Override
  public MutableDatabaseInfo getDatabaseInfo()
  {
    return databaseInfo;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getJdbcDriverInfo()
   */
  @Override
  public MutableJdbcDriverInfo getJdbcDriverInfo()
  {
    return jdbcDriverInfo;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getRoutines()
   */
  @Override
  public Collection<Routine> getRoutines()
  {
    final List<MutableRoutine> values = routines.values();
    return new ArrayList<Routine>(values);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getRoutines()
   */
  @Override
  public Collection<Routine> getRoutines(final Schema schema)
  {
    final FilterBySchema filter = new FilterBySchema(schema);
    return routines.values().stream().filter(filter)
      .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSchema(java.lang.String)
   */
  @Override
  public Optional<Schema> getSchema(final String name)
  {
    return schemas.stream().filter(schema -> schema.getFullName().equals(name))
      .findFirst();
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSchemaCrawlerInfo()
   */
  @Override
  public ImmutableSchemaCrawlerInfo getSchemaCrawlerInfo()
  {
    return schemaCrawlerInfo;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSchemas()
   */
  @Override
  public Collection<Schema> getSchemas()
  {
    final List<Schema> schemas = new ArrayList<Schema>(this.schemas);
    Collections.sort(schemas);
    return schemas;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getSequences()
   */
  @Override
  public Collection<Sequence> getSequences()
  {
    return new ArrayList<Sequence>(sequences.values());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSequences(schemacrawler.schema.Schema)
   */
  @Override
  public Collection<Sequence> getSequences(final Schema schema)
  {
    final FilterBySchema filter = new FilterBySchema(schema);
    return sequences.values().stream().filter(filter)
      .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSynonyms()
   */
  @Override
  public Collection<Synonym> getSynonyms()
  {
    return new ArrayList<Synonym>(synonyms.values());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getRoutines()
   */
  @Override
  public Collection<Synonym> getSynonyms(final Schema schema)
  {
    final FilterBySchema filter = new FilterBySchema(schema);
    return synonyms.values().stream().filter(filter)
      .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSystemColumnDataType(java.lang.String)
   */
  @Override
  public Optional<MutableColumnDataType> getSystemColumnDataType(final String name)
  {
    return lookupColumnDataType(new SchemaReference(), name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#getSystemColumnDataTypes()
   */
  @Override
  public Collection<ColumnDataType> getSystemColumnDataTypes()
  {
    return getColumnDataTypes(new SchemaReference());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getTables()
   */
  @Override
  public Collection<Table> getTables()
  {
    final List<Table> values = new ArrayList<Table>(tables.values());
    return values;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getTables()
   */
  @Override
  public Collection<Table> getTables(final Schema schema)
  {
    final FilterBySchema filter = new FilterBySchema(schema);
    return tables.values().stream().filter(filter).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getColumnDataType(java.lang.String)
   */
  @Override
  public Optional<MutableColumnDataType> lookupColumnDataType(final Schema schema,
                                                              final String name)
  {
    return columnDataTypes.lookup(schema, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<MutableRoutine> lookupRoutine(final Schema schema,
                                                final String name)
  {
    return routines.lookup(schema, name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#lookupSequence(schemacrawler.schema.Schema,
   *      java.lang.String)
   */
  @Override
  public Optional<MutableSequence> lookupSequence(final Schema schemaRef,
                                                  final String name)
  {
    return sequences.lookup(schemaRef, name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Catalog#lookupSynonym(schemacrawler.schema.Schema,
   *      java.lang.String)
   */
  @Override
  public Optional<MutableSynonym> lookupSynonym(final Schema schemaRef,
                                                final String name)
  {
    return synonyms.lookup(schemaRef, name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getTable(java.lang.String)
   */
  @Override
  public Optional<MutableTable> lookupTable(final Schema schemaRef,
                                            final String name)
  {
    return tables.lookup(schemaRef, name);
  }

  @Override
  public <N extends NamedObject> void reduce(final Class<N> clazz,
                                             final Reducer<N> reducer)
  {
    if (reducer == null)
    {
      return;
    }
    else if (Schema.class.isAssignableFrom(clazz))
    {
      ((Reducer<Schema>) reducer).reduce(schemas);
    }
    else if (Table.class.isAssignableFrom(clazz))
    {
      // Filter the list of tables based on grep criteria, and
      // parent-child relationships
      ((Reducer<Table>) reducer).reduce(tables);
    }
    else if (Routine.class.isAssignableFrom(clazz))
    {
      // Filter the list of routines based on grep criteria
      ((Reducer<Routine>) reducer).reduce(routines);
    }
    else if (Synonym.class.isAssignableFrom(clazz))
    {
      ((Reducer<Synonym>) reducer).reduce(synonyms);
    }
    else if (Sequence.class.isAssignableFrom(clazz))
    {
      ((Reducer<Sequence>) reducer).reduce(sequences);
    }
  }

  void addColumnDataType(final MutableColumnDataType columnDataType)
  {
    if (columnDataType != null)
    {
      columnDataTypes.add(columnDataType);
    }
  }

  void addRoutine(final MutableRoutine routine)
  {
    routines.add(routine);
  }

  Schema addSchema(final Schema schema)
  {
    schemas.add(schema);
    return schema;
  }

  Schema addSchema(final String catalogName, final String schemaName)
  {
    return addSchema(new SchemaReference(catalogName, schemaName));
  }

  void addSequence(final MutableSequence sequence)
  {
    sequences.add(sequence);
  }

  void addSynonym(final MutableSynonym synonym)
  {
    synonyms.add(synonym);
  }

  void addTable(final MutableTable table)
  {
    tables.add(table);
  }

  NamedObjectList<MutableRoutine> getAllRoutines()
  {
    return routines;
  }

  NamedObjectList<MutableSequence> getAllSequences()
  {
    return sequences;
  }

  NamedObjectList<MutableSynonym> getAllSynonyms()
  {
    return synonyms;
  }

  NamedObjectList<MutableTable> getAllTables()
  {
    return tables;
  }

  Collection<Schema> getSchemaNames()
  {
    return schemas;
  }

  MutableColumnDataType lookupColumnDataTypeByType(final int type)
  {
    return columnDataTypes.lookupColumnDataTypeByType(type);
  }

  void setCrawlHeaderInfo(final String title)
  {
    crawlHeaderInfo = new ImmutableCrawlHeaderInfo(schemaCrawlerInfo,
                                                   jdbcDriverInfo,
                                                   databaseInfo,
                                                   title);
  }

}
