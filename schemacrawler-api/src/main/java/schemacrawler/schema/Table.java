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

package schemacrawler.schema;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Represents a table in the database.
 *
 * @author Sualeh Fatehi
 */
public interface Table
  extends DatabaseObject, TypedObject<TableType>, DefinedObject
{

  /**
   * Gets a column by unqualified name.
   *
   * @param name
   *        Unqualified name
   * @return Column, or null if not found.
   */
  @Deprecated
  default Column getColumn(final String name)
  {
    return lookupColumn(name).orElse(null);
  }

  /**
   * Gets the list of columns in ordinal order.
   *
   * @return Columns of the table
   */
  List<Column> getColumns();

  /**
   * Gets the list of exported foreign keys. That is, only those whose
   * primary key is referenced in another table.
   *
   * @return Exported foreign keys of the table.
   */
  Collection<ForeignKey> getExportedForeignKeys();

  /**
   * Gets a foreign key by name.
   *
   * @param name
   *        Name
   * @return Foreign key, or null if not found.
   */
  @Deprecated
  default ForeignKey getForeignKey(final String name)
  {
    return lookupForeignKey(name).orElse(null);
  }

  /**
   * Gets the list of foreign keys. Same as calling
   * getForeignKeys(TableAssociationType.all).
   *
   * @return Foreign keys of the table.
   */
  Collection<ForeignKey> getForeignKeys();

  /**
   * Gets the list of imported foreign keys. That is, only those that
   * reference a primary key another table.
   *
   * @return Imported foreign keys of the table.
   */
  Collection<ForeignKey> getImportedForeignKeys();

  /**
   * Gets an index by unqualified name.
   *
   * @param name
   *        Name
   * @return Index, or null if not found.
   */
  @Deprecated
  default Index getIndex(final String name)
  {
    return lookupIndex(name).orElse(null);
  }

  /**
   * Gets the list of indexes.
   *
   * @return Indexes of the table.
   */
  Collection<Index> getIndexes();

  /**
   * Gets the primary key.
   *
   * @return Primary key
   */
  PrimaryKey getPrimaryKey();

  /**
   * Gets a privilege by unqualified name.
   *
   * @param name
   *        Unqualified name
   * @return Privilege, or null if not found.
   */
  @Deprecated
  default Privilege<Table> getPrivilege(final String name)
  {
    return lookupPrivilege(name).orElse(null);
  }

  /**
   * Gets the list of privileges.
   *
   * @return Privileges for the table.
   */
  Collection<Privilege<Table>> getPrivileges();

  /**
   * Gets the tables related to this one, based on the specified
   * relationship type. Child tables are those who have a foreign key
   * from this table. Parent tables are those to which this table has a
   * foreign key.
   *
   * @param tableRelationshipType
   *        Table relationship type
   * @return Related tables.
   */
  Collection<Table> getRelatedTables(final TableRelationshipType tableRelationshipType);

  /**
   * Gets the constraints for the table.
   *
   * @return Constraints for the table
   */
  Collection<TableConstraint> getTableConstraints();

  /**
   * Gets the table type.
   *
   * @return Table type.
   */
  TableType getTableType();

  /**
   * Gets a trigger by unqualified name.
   *
   * @param name
   *        Unqualified name
   * @return Trigger, or null if not found.
   */
  @Deprecated
  default Trigger getTrigger(final String name)
  {
    return lookupTrigger(name).orElse(null);
  }

  /**
   * Gets the list of triggers.
   *
   * @return Triggers for the table.
   */
  Collection<Trigger> getTriggers();

  /**
   * Gets a column by unqualified name.
   *
   * @param name
   *        Unqualified name
   * @return Column.
   */
  Optional<? extends Column> lookupColumn(String name);

  /**
   * Gets a foreign key by name.
   *
   * @param name
   *        Name
   * @return Foreign key.
   */
  Optional<? extends ForeignKey> lookupForeignKey(String name);

  /**
   * Gets an index by unqualified name.
   *
   * @param name
   *        Name
   * @return Index.
   */
  Optional<? extends Index> lookupIndex(String name);

  /**
   * Gets a privilege by unqualified name.
   *
   * @param name
   *        Name
   * @return Privilege.
   */
  Optional<? extends Privilege<Table>> lookupPrivilege(String name);

  /**
   * Gets a trigger by unqualified name.
   *
   * @param name
   *        Name
   * @return Trigger.
   */
  Optional<? extends Trigger> lookupTrigger(String name);

}
