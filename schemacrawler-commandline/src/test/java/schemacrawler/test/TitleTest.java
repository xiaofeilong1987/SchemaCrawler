package schemacrawler.test;


import static java.nio.file.Files.newBufferedWriter;
import static org.junit.Assert.fail;
import static schemacrawler.test.utility.TestUtility.createTempFile;
import static sf.util.commandlineparser.CommandLineUtility.flattenCommandlineArgs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import schemacrawler.Main;
import schemacrawler.schemacrawler.Config;
import schemacrawler.test.utility.BaseDatabaseTest;
import schemacrawler.test.utility.TestWriter;
import schemacrawler.tools.integration.graph.GraphOutputFormat;
import schemacrawler.tools.options.OutputFormat;
import schemacrawler.tools.options.TextOutputFormat;

public class TitleTest
  extends BaseDatabaseTest
{

  private static final String TITLE_OUTPUT = "title_output/";

  @Test
  public void commandLineWithTitle()
    throws Exception
  {
    final OutputFormat[] outputFormats = new OutputFormat[] {
        TextOutputFormat.text,
        TextOutputFormat.html,
        TextOutputFormat.json,
        GraphOutputFormat.dot
    };

    final Map<String, String> args = new HashMap<String, String>();
    args.put("title", "Database Design for Books and Publishers");
    args.put("routines", "");
    // Testing no sequences, synonyms

    final List<String> failures = new ArrayList<>();
    for (final String command: new String[] {
        "schema", "list"
    })
    {
      for (final OutputFormat outputFormat: outputFormats)
      {
        run(args,
            null,
            command,
            outputFormat,
            "commandLineWithTitle_" + command + "." + outputFormat.getFormat(),
            failures);
      }
    }

    if (!failures.isEmpty())
    {
      fail(failures.toString());
    }
  }

  private Path createConfig(final Map<String, String> config)
    throws IOException
  {
    final String prefix = "SchemaCrawler.TestCommandLineConfig";
    final Path configFile = createTempFile(prefix, "properties");
    final Properties configProperties = new Properties();
    configProperties.putAll(config);
    configProperties
      .store(newBufferedWriter(configFile, StandardCharsets.UTF_8), prefix);
    return configFile;
  }

  private void run(final Map<String, String> argsMap,
                   final Map<String, String> config,
                   final String command,
                   final OutputFormat outputFormat,
                   final String referenceFile,
                   final List<String> allFailures)
    throws Exception
  {

    try (final TestWriter out = new TestWriter(outputFormat.getFormat());)
    {
      argsMap.put("url", "jdbc:hsqldb:hsql://localhost/schemacrawler");
      argsMap.put("user", "sa");
      argsMap.put("password", "");
      argsMap.put("infolevel", "maximum");
      argsMap.put("command", command);
      argsMap.put("outputformat", outputFormat.getFormat());
      argsMap.put("outputfile", out.toString());

      final Config runConfig = new Config();
      final Config informationSchema = Config
        .loadResource("/hsqldb.INFORMATION_SCHEMA.config.properties");
      runConfig.putAll(informationSchema);
      if (config != null)
      {
        runConfig.putAll(config);
      }

      final Path configFile = createConfig(runConfig);
      argsMap.put("g", configFile.toString());

      Main.main(flattenCommandlineArgs(argsMap));

      final List<String> failures = out.collectFailures(TITLE_OUTPUT
                                                        + referenceFile);
      allFailures.addAll(failures);
    }
  }

}
