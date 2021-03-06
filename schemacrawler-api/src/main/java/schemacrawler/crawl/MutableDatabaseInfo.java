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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import schemacrawler.schema.DatabaseInfo;
import schemacrawler.schema.DatabaseProperty;

/**
 * Database and connection information. Created from metadata returned
 * by a JDBC call, and other sources of information.
 *
 * @author Sualeh Fatehi sualeh@hotmail.com
 */
final class MutableDatabaseInfo
  implements DatabaseInfo
{

  private static final long serialVersionUID = 4051323422934251828L;

  private String userName;
  private String productName;
  private String productVersion;
  private final Set<DatabaseProperty> databaseProperties = new HashSet<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductName()
  {
    return productName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductVersion()
  {
    return productVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<DatabaseProperty> getProperties()
  {
    final List<DatabaseProperty> properties = new ArrayList<>(databaseProperties);
    Collections.sort(properties);
    return properties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserName()
  {
    return userName;
  }

  /**
   * {@inheritDoc}
   *
   * @see Object#toString()
   */
  @Override
  public String toString()
  {
    final StringBuilder info = new StringBuilder();
    info.append("-- database: ").append(getProductName()).append(' ')
      .append(getProductVersion()).append(System.lineSeparator());
    return info.toString();
  }

  void addAll(final Collection<ImmutableDatabaseProperty> dbProperties)
  {
    if (dbProperties != null)
    {
      databaseProperties.addAll(dbProperties);
    }
  }

  void setProductName(final String productName)
  {
    this.productName = productName;
  }

  void setProductVersion(final String productVersion)
  {
    this.productVersion = productVersion;
  }

  void setUserName(final String userName)
  {
    this.userName = userName;
  }

}
