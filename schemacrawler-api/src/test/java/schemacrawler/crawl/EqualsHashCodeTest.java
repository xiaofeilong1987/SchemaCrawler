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

package schemacrawler.crawl;


import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import schemacrawler.schema.SchemaReference;

@Ignore("Slow test")
public class EqualsHashCodeTest
{

  @Test
  public void schemaRef()
  {
    final SchemaReference[] schemaRefs = new SchemaReference[] {
        new SchemaReference("catalog", "schema"),
        new SchemaReference(null, "schema"),
        new SchemaReference("catalog", null)
    };
    for (final SchemaReference schemaRef: schemaRefs)
    {
      assertEquals(schemaRef.toString(), schemaRef.getFullName());
    }
  }

}
