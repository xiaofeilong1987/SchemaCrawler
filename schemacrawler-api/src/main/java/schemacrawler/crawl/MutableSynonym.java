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
import schemacrawler.schema.DatabaseObject;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Synonym;

/**
 * Represents a database synonym. Created from metadata returned by a
 * JDBC call.
 *
 * @author Matt Albrecht, Sualeh Fatehi
 */
final class MutableSynonym
  extends AbstractDatabaseObject
  implements Synonym
{

  private static final long serialVersionUID = -5980593047288755771L;

  private DatabaseObject referencedObject;

  MutableSynonym(final Schema schema, final String name)
  {
    super(schema, name);
  }

  @Override
  public DatabaseObject getReferencedObject()
  {
    return referencedObject;
  }

  void setReferencedObject(final DatabaseObject referencedObject)
  {
    this.referencedObject = requireNonNull(referencedObject,
                                           "Referenced object not provided");
  }

}
