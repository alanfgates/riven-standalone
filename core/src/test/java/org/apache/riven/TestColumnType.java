/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.riven;

import org.junit.Assert;
import org.junit.Test;

public class TestColumnType {

  @Test
  public void typeNameParsing() {
    Assert.assertEquals("int", ColumnType.getTypeName("int"));
    Assert.assertEquals("bigint", ColumnType.getTypeName("BIGINT"));
    Assert.assertEquals("varchar", ColumnType.getTypeName("varchar(256)"));
    Assert.assertEquals("array", ColumnType.getTypeName("array<Integer>"));
    Assert.assertEquals("int", ColumnType.getTypeName("Integer"));
    Assert.assertEquals("decimal", ColumnType.getTypeName("numeric(36)"));
  }

  @Test
  public void typesCompatibles() {
    Assert.assertTrue(ColumnType.areColTypesCompatible(ColumnType.BIGINT_TYPE_NAME,
        ColumnType.BIGINT_TYPE_NAME));
    Assert.assertTrue(ColumnType.areColTypesCompatible(ColumnType.VARCHAR_TYPE_NAME,
        ColumnType.STRING_TYPE_NAME));
    Assert.assertTrue(ColumnType.areColTypesCompatible(ColumnType.VARCHAR_TYPE_NAME,
        ColumnType.DOUBLE_TYPE_NAME));
    Assert.assertTrue(ColumnType.areColTypesCompatible(ColumnType.VOID_TYPE_NAME,
        ColumnType.BOOLEAN_TYPE_NAME));
    Assert.assertTrue(ColumnType.areColTypesCompatible(ColumnType.TIMESTAMP_TYPE_NAME,
        ColumnType.STRING_TYPE_NAME));
    Assert.assertTrue(ColumnType.areColTypesCompatible(ColumnType.DECIMAL_TYPE_NAME,
        ColumnType.STRING_TYPE_NAME));
    Assert.assertTrue(ColumnType.areColTypesCompatible(ColumnType.TINYINT_TYPE_NAME,
        ColumnType.DECIMAL_TYPE_NAME));
    Assert.assertFalse(ColumnType.areColTypesCompatible(ColumnType.STRING_TYPE_NAME,
        ColumnType.DATE_TYPE_NAME));
    Assert.assertFalse(ColumnType.areColTypesCompatible(ColumnType.DOUBLE_TYPE_NAME,
        ColumnType.BIGINT_TYPE_NAME));
    Assert.assertFalse(ColumnType.areColTypesCompatible(ColumnType.LIST_TYPE_NAME,
        ColumnType.MAP_TYPE_NAME));
  }
}
