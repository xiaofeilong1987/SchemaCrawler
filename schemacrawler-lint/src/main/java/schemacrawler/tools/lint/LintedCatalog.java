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
package schemacrawler.tools.lint;


import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.BaseCatalogDecorator;
import schemacrawler.schemacrawler.SchemaCrawlerException;

public final class LintedCatalog
  extends BaseCatalogDecorator
{

  private static final Logger LOGGER = Logger.getLogger(LintedCatalog.class
    .getName());

  private static final long serialVersionUID = -3953296149824921463L;

  private final LintCollector collector;

  public LintedCatalog(final Catalog catalog, final LinterConfigs linterConfigs)
    throws SchemaCrawlerException
  {
    super(catalog);

    collector = new SimpleLintCollector();

    final LinterRegistry registry = new LinterRegistry();
    for (final String linterId: registry)
    {
      final Linter linter = registry.lookupLinter(linterId);
      LOGGER.log(Level.FINE,
                 String.format("Linting with %s", linter.getClass().getName()));
      linter.setLintCollector(collector);
      // Configure linter
      if (linterConfigs != null)
      {
        linter.config(linterConfigs.get(linter.getId()));
      }
      // Do linting
      if (linter.getSeverity() != LintSeverity.off)
      {
        linter.lint(catalog);
      }
    }

  }

  public LintCollector getCollector()
  {
    return collector;
  }

}
