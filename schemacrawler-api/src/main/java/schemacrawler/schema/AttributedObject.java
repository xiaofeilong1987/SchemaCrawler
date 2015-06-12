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

package schemacrawler.schema;


import java.util.Map;

public interface AttributedObject
{

  /**
   * Gets an attribute.
   *
   * @param name
   *        Attribute name.
   * @return Attribute value.
   */
  Object getAttribute(String name);

  /**
   * Gets an attribute.
   *
   * @param name
   *        Attribute name.
   * @return Attribute value.
   */
  <T> T getAttribute(String name, T defaultValue);

  /**
   * Gets all attributes.
   *
   * @return Map of attributes
   */
  Map<String, Object> getAttributes();

  /**
   * Removes an attribute.
   *
   * @param name
   *        Attribute name
   */
  void removeAttribute(String name);

  /**
   * Sets an attribute.
   *
   * @param name
   *        Attribute name
   * @param value
   *        Attribute value
   */
  void setAttribute(String name, Object value);

}
