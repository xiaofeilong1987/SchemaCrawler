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

package schemacrawler.tools.executable;


import static sf.util.Utility.isBlank;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.options.OutputOptions;
import sf.util.ObjectToString;

/**
 * A SchemaCrawler tools executable unit.
 *
 * @author Sualeh Fatehi
 */
public abstract class BaseExecutable
  implements Executable
{

  protected final String command;
  protected SchemaCrawlerOptions schemaCrawlerOptions;
  protected OutputOptions outputOptions;
  protected Config additionalConfiguration;

  protected BaseExecutable(final String command)
  {
    if (isBlank(command))
    {
      throw new IllegalArgumentException("No command specified");
    }
    this.command = command;

    schemaCrawlerOptions = new SchemaCrawlerOptions();
    outputOptions = new OutputOptions();
  }

  @Override
  public final Config getAdditionalConfiguration()
  {
    return additionalConfiguration;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.tools.executable.Executable#getCommand()
   */
  @Override
  public final String getCommand()
  {
    return command;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.tools.executable.Executable#getOutputOptions()
   */
  @Override
  public final OutputOptions getOutputOptions()
  {
    return outputOptions;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.tools.executable.Executable#getSchemaCrawlerOptions()
   */
  @Override
  public final SchemaCrawlerOptions getSchemaCrawlerOptions()
  {
    return schemaCrawlerOptions;
  }

  @Override
  public final void setAdditionalConfiguration(final Config additionalConfiguration)
  {
    if (additionalConfiguration == null)
    {
      this.additionalConfiguration = new Config();
    }
    else
    {
      this.additionalConfiguration = additionalConfiguration;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.tools.executable.Executable#setOutputOptions(schemacrawler.tools.options.OutputOptions)
   */
  @Override
  public final void setOutputOptions(final OutputOptions outputOptions)
  {
    if (outputOptions != null)
    {
      this.outputOptions = outputOptions;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.tools.executable.Executable#setSchemaCrawlerOptions(schemacrawler.schemacrawler.SchemaCrawlerOptions)
   */
  @Override
  public final void setSchemaCrawlerOptions(final SchemaCrawlerOptions schemaCrawlerOptions)
  {
    if (schemaCrawlerOptions != null)
    {
      this.schemaCrawlerOptions = schemaCrawlerOptions;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see Object#toString()
   */
  @Override
  public final String toString()
  {
    return ObjectToString.toString(this);
  }

}
