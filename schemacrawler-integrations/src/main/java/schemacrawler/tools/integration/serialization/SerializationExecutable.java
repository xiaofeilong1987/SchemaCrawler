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

package schemacrawler.tools.integration.serialization;


import java.io.Writer;
import java.sql.Connection;

import schemacrawler.schema.Catalog;
import schemacrawler.tools.executable.BaseStagedExecutable;

/**
 * Main executor for the graphing integration.
 *
 * @author Sualeh Fatehi
 */
public final class SerializationExecutable
  extends BaseStagedExecutable
{

  static final String COMMAND = "serialize";

  public SerializationExecutable()
  {
    this(COMMAND);
  }

  public SerializationExecutable(final String command)
  {
    super(command);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeOn(final Catalog db, final Connection connection)
    throws Exception
  {
    final SerializableCatalog catalog = new XmlSerializedCatalog(db);
    outputOptions.forceCompressedOutputFile();
    try (final Writer writer = outputOptions.openNewOutputWriter();)
    {
      catalog.save(writer);
    }
  }

}
