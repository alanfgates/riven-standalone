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
package org.apache.riven.client.builder;

import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.Order;
import org.apache.riven.api.Partition;
import org.apache.riven.api.PrincipalPrivilegeSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartitionBuilder extends StorageDescriptorBuilder<PartitionBuilder> {
  private String dbName, tableName;
  private int createTime, lastAccessTime;
  private Map<String, String> partParams;
  private List<String> values;

  public PartitionBuilder() {
    // Set some reasonable defaults
    partParams = new HashMap<>();
    createTime = lastAccessTime = 0;
    super.setChild(this);
  }

  public PartitionBuilder setDbName(String dbName) {
    this.dbName = dbName;
    return this;
  }

  public PartitionBuilder setTableName(String tableName) {
    this.tableName = tableName;
    return this;
  }

  public PartitionBuilder setValues(List<String> values) {
    this.values = values;
    return this;
  }

  public PartitionBuilder setCreateTime(int createTime) {
    this.createTime = createTime;
    return this;
  }

  public PartitionBuilder setLastAccessTime(int lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
    return this;
  }

  public PartitionBuilder setPartParams(Map<String, String> partParams) {
    this.partParams = partParams;
    return this;
  }

  public PartitionBuilder addPartParam(String key, String value) {
    if (partParams == null) partParams = new HashMap<>();
    partParams.put(key, value);
    return this;
  }

  public Partition build() {
    assert dbName != null : "dbName must be set";
    assert tableName != null : "tableName must be set";
    assert values != null : "values must be set";
    Partition p = new Partition(values, dbName, tableName, createTime, lastAccessTime, buildSd(),
        partParams);
    p.setPrivileges(buildPPS());
    return p;
  }
}
