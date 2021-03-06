/*
 * SchemaCrawler
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package schemacrawler.test;


import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.ConnectionOptions;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.tools.options.InfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;
import sf.util.ObjectToString;

public abstract class AbstractSchemaCrawlerSystemTest
{

  protected final ApplicationContext appContext = new ClassPathXmlApplicationContext("datasources.xml");
  protected final String[] dataSources = {
      "MicrosoftSQLServer", "Oracle", "IBM_DB2", "MySQL", "PostgreSQL",
  // "Derby",
  };

  @Test
  public void connections()
    throws Exception
  {
    final List<String> connectionErrors = new ArrayList<>();
    for (final String dataSource: dataSources)
    {
      try
      {
        connect(dataSource);
      }
      catch (final Exception e)
      {
        final String message = dataSource + ": " + e.getMessage();
        System.out.println(message);
        connectionErrors.add(message);
      }
    }
    if (!connectionErrors.isEmpty())
    {
      final String error = ObjectToString.toString(connectionErrors);
      System.out.println(error);
      fail(error);
    }
  }

  protected Connection connect(final String dataSourceName)
    throws Exception
  {
    final ConnectionOptions connectionOptions = (ConnectionOptions) appContext
      .getBean(dataSourceName);
    final Connection connection = connectionOptions.getConnection();
    return connection;
  }

  protected SchemaCrawlerOptions createOptions(final String dataSourceName,
                                               final String schemaInclusion)
  {
    final Config config = (Config) appContext.getBean(dataSourceName
                                                      + ".properties");
    final SchemaCrawlerOptionsBuilder optionsBuilder = new SchemaCrawlerOptionsBuilder()
      .setFromConfig(config);
    optionsBuilder.schemaInfoLevel(InfoLevel.maximum.getSchemaInfoLevel());
    if (schemaInclusion != null)
    {
      optionsBuilder
        .includeSchemas(new RegularExpressionInclusionRule(schemaInclusion));
    }
    return optionsBuilder.toOptions();
  }

  protected Catalog retrieveDatabase(final String dataSourceName,
                                     final SchemaCrawlerOptions schemaCrawlerOptions)
    throws Exception
  {
    final Connection connection = connect(dataSourceName);
    try
    {
      final Catalog catalog = SchemaCrawlerUtility
        .getCatalog(connection, schemaCrawlerOptions);
      return catalog;
    }
    catch (final Exception e)
    {
      throw new SchemaCrawlerException(dataSourceName, e);
    }
  }

}
