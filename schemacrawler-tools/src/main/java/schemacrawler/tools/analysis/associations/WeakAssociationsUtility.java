/*
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 * This library is free software; you can redistribute it and/or modify it under
 * the terms
 * of the GNU Lesser General Public License as published by the Free Software
 * Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package schemacrawler.tools.analysis.associations;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import schemacrawler.schema.Table;

public class WeakAssociationsUtility
{

  private static final String WEAK_ASSOCIATIONS_KEY = "schemacrawler.weak_associations";

  public static final Collection<WeakAssociationForeignKey> getWeakAssociations(final Table table)
  {
    if (table == null)
    {
      return null;
    }

    final SortedSet<WeakAssociationForeignKey> weakAssociations = table
      .getAttribute(WEAK_ASSOCIATIONS_KEY,
                    new TreeSet<WeakAssociationForeignKey>());
    final List<WeakAssociationForeignKey> weakAssociationsList = new ArrayList<>(weakAssociations);
    Collections.sort(weakAssociationsList);
    return weakAssociationsList;
  }

  static void addWeakAssociationToTable(final Table table,
                                        final WeakAssociationForeignKey weakAssociation)
  {
    if (table != null && weakAssociation != null)
    {
      final SortedSet<WeakAssociationForeignKey> tableWeakAssociations = table
        .getAttribute(WEAK_ASSOCIATIONS_KEY,
                      new TreeSet<WeakAssociationForeignKey>());

      tableWeakAssociations.add(weakAssociation);

      table.setAttribute(WEAK_ASSOCIATIONS_KEY, tableWeakAssociations);
    }
  }

  private WeakAssociationsUtility()
  {
  }

}
