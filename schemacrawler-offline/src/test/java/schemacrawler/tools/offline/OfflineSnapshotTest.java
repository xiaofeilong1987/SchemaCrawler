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
package schemacrawler.tools.offline;


import static java.nio.file.Files.size;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static schemacrawler.test.utility.TestUtility.createTempFile;
import static sf.util.commandlineparser.CommandLineUtility.flattenCommandlineArgs;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.test.utility.BaseExecutableTest;
import schemacrawler.test.utility.TestWriter;
import schemacrawler.tools.integration.serialization.XmlSerializedCatalog;
import schemacrawler.tools.iosource.CompressedFileOutputResource;
import schemacrawler.tools.iosource.OutputWriter;
import schemacrawler.tools.options.OutputOptions;

public class OfflineSnapshotTest
  extends BaseExecutableTest
{

  private static final String OFFLINE_EXECUTABLE_OUTPUT = "offline_executable_output/";
  private Path serializedDatabaseFile;

  @Test
  public void offlineSnapshotCommandLine()
    throws Exception
  {
    try (final TestWriter out = new TestWriter("text");)
    {
      final Map<String, String> argsMap = new HashMap<>();
      argsMap.put("database", serializedDatabaseFile.toString());

      argsMap.put("infolevel", "maximum");
      argsMap.put("command", "details");
      argsMap.put("outputformat", "text");
      argsMap.put("outputfile", out.toString());

      final OfflineSnapshotCommandLine commandLine = new OfflineSnapshotCommandLine(flattenCommandlineArgs(argsMap));
      commandLine.execute();

      out.assertEquals(OFFLINE_EXECUTABLE_OUTPUT + "details.txt");
    }
  }

  @Test
  public void offlineSnapshotCommandLineWithFilters()
    throws Exception
  {
    try (final TestWriter out = new TestWriter("text");)
    {
      final Map<String, String> argsMap = new HashMap<>();
      argsMap.put("database", serializedDatabaseFile.toString());

      argsMap.put("noinfo", "true");
      argsMap.put("infolevel", "maximum");
      argsMap.put("command", "details");
      argsMap.put("outputformat", "text");
      argsMap.put("routines", "");
      argsMap.put("tables", ".*SALES");
      argsMap.put("outputfile", out.toString());

      final OfflineSnapshotCommandLine commandLine = new OfflineSnapshotCommandLine(flattenCommandlineArgs(argsMap));
      commandLine.execute();

      out.assertEquals(OFFLINE_EXECUTABLE_OUTPUT + "offlineWithFilters.txt");
    }
  }

  @Test
  public void offlineSnapshotCommandLineWithSchemaFilters()
    throws Exception
  {
    try (final TestWriter out = new TestWriter("text");)
    {
      final Map<String, String> args = new HashMap<>();
      args.put("database", serializedDatabaseFile.toString());

      args.put("noinfo", "true");
      args.put("infolevel", "maximum");
      args.put("command", "list");
      args.put("outputformat", "text");
      args.put("schemas", "PUBLIC.BOOKS");
      args.put("outputfile", out.toString());

      final List<String> argsList = new ArrayList<>();
      for (final Map.Entry<String, String> arg: args.entrySet())
      {
        argsList.add(String.format("-%s=%s", arg.getKey(), arg.getValue()));
      }

      final OfflineSnapshotCommandLine commandLine = new OfflineSnapshotCommandLine(argsList
        .toArray(new String[0]));
      commandLine.execute();

      out.assertEquals(OFFLINE_EXECUTABLE_OUTPUT
                       + "offlineWithSchemaFilters.txt");
    }
  }

  @Test
  public void offlineSnapshotExecutable()
    throws Exception
  {

    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.maximum());

    final OutputOptions inputOptions = new OutputOptions();
    inputOptions.setCompressedInputFile(serializedDatabaseFile);

    final OfflineSnapshotExecutable executable = new OfflineSnapshotExecutable("details");
    executable.setSchemaCrawlerOptions(schemaCrawlerOptions);
    executable.setInputOptions(inputOptions);

    executeExecutable(executable, "text", OFFLINE_EXECUTABLE_OUTPUT
                                          + "details.txt");
  }

  @Before
  public void serializeCatalog()
    throws SchemaCrawlerException, IOException
  {

    final SchemaCrawlerOptions schemaCrawlerOptions = new SchemaCrawlerOptions();
    schemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.maximum());

    final Catalog catalog = getCatalog(schemaCrawlerOptions);
    assertNotNull("Could not obtain catalog", catalog);
    assertTrue("Could not find any schemas", catalog.getSchemas().size() > 0);

    final Schema schema = catalog.getSchema("PUBLIC.BOOKS").orElse(null);
    assertNotNull("Could not obtain schema", schema);
    assertEquals("Unexpected number of tables in the schema", 6, catalog
      .getTables(schema).size());

    serializedDatabaseFile = createTempFile("schemacrawler", "ser");

    final XmlSerializedCatalog xmlDatabase = new XmlSerializedCatalog(catalog);
    final Writer writer = new OutputWriter(new CompressedFileOutputResource(serializedDatabaseFile),
                                           StandardCharsets.UTF_8);
    xmlDatabase.save(writer);
    writer.close();
    assertNotSame("Database was not serialized to XML",
                  0,
                  size(serializedDatabaseFile));

  }

}
