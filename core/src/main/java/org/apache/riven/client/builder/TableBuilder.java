/**
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
package org.apache.riven.client.builder;

import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.Order;
import org.apache.riven.api.PrincipalPrivilegeSet;
import org.apache.riven.api.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBuilder extends StorageDescriptorBuilder<TableBuilder> {
  private String dbName, tableName, owner, viewOriginalText, viewExpandedText, type;
  private List<FieldSchema> partCols;
  private int createTime, lastAccessTime, retention;
  private Map<String, String> tableParams;
  private boolean rewriteEnabled, temporary;

  public TableBuilder() {
    // Set some reasonable defaults
    tableParams = new HashMap<>();
    owner = "me";
    createTime = lastAccessTime = retention = 0;
    super.setChild(this);
  }

  public TableBuilder setDbName(String dbName) {
    this.dbName = dbName;
    return this;
  }

  public TableBuilder setTableName(String tableName) {
    this.tableName = tableName;
    return this;
  }

  public TableBuilder setOwner(String owner) {
    this.owner = owner;
    return this;
  }

  public TableBuilder setViewOriginalText(String viewOriginalText) {
    this.viewOriginalText = viewOriginalText;
    return this;
  }

  public TableBuilder setViewExpandedText(String viewExpandedText) {
    this.viewExpandedText = viewExpandedText;
    return this;
  }

  public TableBuilder setType(String type) {
    this.type = type;
    return this;
  }

  public TableBuilder setPartCols(List<FieldSchema> partCols) {
    this.partCols = partCols;
    return this;
  }

  public TableBuilder addPartCol(String name, String type, String comment) {
    if (partCols == null) partCols = new ArrayList<>();
    partCols.add(new FieldSchema(name, type, comment));
    return this;
  }

  public TableBuilder addPartCol(String name, String type) {
    return addPartCol(name, type, "");
  }

  public TableBuilder setCreateTime(int createTime) {
    this.createTime = createTime;
    return this;
  }

  public TableBuilder setLastAccessTime(int lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
    return this;
  }

  public TableBuilder setRetention(int retention) {
    this.retention = retention;
    return this;
  }

  public TableBuilder setTableParams(Map<String, String> tableParams) {
    this.tableParams = tableParams;
    return this;
  }

  public TableBuilder addTableParam(String key, String value) {
    if (tableParams == null) tableParams = new HashMap<>();
    tableParams.put(key, value);
    return this;
  }

  public TableBuilder setRewriteEnabled(boolean rewriteEnabled) {
    this.rewriteEnabled = rewriteEnabled;
    return this;
  }

  public TableBuilder setTemporary(boolean temporary) {
    this.temporary = temporary;
    return this;
  }

  public Table build() {
    assert dbName != null : "dbName must be set";
    assert tableName != null : "tableName must be set";
    Table t = new Table(tableName, dbName, owner, createTime, lastAccessTime, retention, buildSd(),
        partCols, tableParams, viewOriginalText, viewExpandedText, type);
    if (rewriteEnabled) t.setRewriteEnabled(true);
    if (temporary) t.setTemporary(temporary);
    t.setPrivileges(buildPPS());
    return t;
  }
}
