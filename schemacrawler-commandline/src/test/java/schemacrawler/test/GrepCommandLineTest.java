package schemacrawler.test;


import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.junit.Assert.fail;
import static schemacrawler.test.utility.TestUtility.clean;
import static schemacrawler.test.utility.TestUtility.compareOutput;
import static schemacrawler.test.utility.TestUtility.createTempFile;

import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import schemacrawler.Main;
import schemacrawler.test.utility.BaseDatabaseTest;
import schemacrawler.tools.options.InfoLevel;
import schemacrawler.tools.options.OutputFormat;
import schemacrawler.tools.options.TextOutputFormat;
import schemacrawler.tools.text.schema.SchemaTextDetailType;

public class GrepCommandLineTest
  extends BaseDatabaseTest
{

  private static final String GREP_OUTPUT = "grep_output/";

  @Test
  public void grep()
    throws Exception
  {
    clean(GREP_OUTPUT);

    final List<String> failures = new ArrayList<>();

    final String[][] grepArgs = new String[][] {
        new String[] {
            "-grepcolumns=.*\\.STREET|.*\\.PRICE", "-routines=",
        },
        new String[] {
            "-grepcolumns=.*\\..*NAME", "-routines=",
        },
        new String[] {
            "-grepdef=.*book authors.*", "-routines=",
        },
        new String[] {
            "-tables=", "-grepinout=.*\\.B_COUNT",
        },
        new String[] {
            "-tables=", "-grepinout=.*\\.B_OFFSET",
        },
        new String[] {
            "-grepcolumns=.*\\.STREET|.*\\.PRICE",
            "-grepdef=.*book authors.*",
            "-routines=",
        },
    };
    for (int i = 0; i < grepArgs.length; i++)
    {
      final String[] grepArgsForRun = grepArgs[i];

      final SchemaTextDetailType schemaTextDetailType = SchemaTextDetailType.details;
      final InfoLevel infoLevel = InfoLevel.detailed;
      final Path additionalProperties = createTempFile("hsqldb.INFORMATION_SCHEMA.config",
                                                       "properties");
      final Writer writer = newBufferedWriter(additionalProperties,
                                              StandardCharsets.UTF_8,
                                              WRITE,
                                              CREATE,
                                              TRUNCATE_EXISTING);
      final Properties properties = new Properties();
      properties.load(this.getClass()
        .getResourceAsStream("/hsqldb.INFORMATION_SCHEMA.config.properties"));
      properties.store(writer, this.getClass().getName());

      final String referenceFile = String.format("grep%02d.txt", i + 1);

      final Path testOutputFile = createTempFile(referenceFile, "data");

      final OutputFormat outputFormat = TextOutputFormat.text;

      final List<String> args = new ArrayList<>(Arrays.asList(new String[] {
          "-url=jdbc:hsqldb:hsql://localhost/schemacrawler",
          "-user=sa",
          "-password=",
          "-g=" + additionalProperties.toString(),
          "-infolevel=" + infoLevel,
          "-command=" + schemaTextDetailType,
          "-outputformat=" + outputFormat.getFormat(),
          "-outputfile=" + testOutputFile.toString(),
          "-noinfo",
      }));
      args.addAll(Arrays.asList(grepArgsForRun));

      Main.main(args.toArray(new String[args.size()]));

      failures.addAll(compareOutput(GREP_OUTPUT + referenceFile,
                                    testOutputFile,
                                    outputFormat.getFormat()));
    }

    if (failures.size() > 0)
    {
      fail(failures.toString());
    }
  }
}
