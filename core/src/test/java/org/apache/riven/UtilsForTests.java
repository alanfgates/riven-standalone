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

import org.apache.riven.api.Database;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.Index;
import org.apache.riven.api.Order;
import org.apache.riven.api.Partition;
import org.apache.riven.api.SerDeInfo;
import org.apache.riven.api.SkewedInfo;
import org.apache.riven.api.StorageDescriptor;
import org.apache.riven.api.Table;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UtilsForTests {
  static final String SERDE_LIB = "org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe";
  static final String INPUT_FORMAT = "org.apache.hadoop.hive.ql.io.HiveInputFormat";
  static final String OUTPUT_FORMAT = "org.apache.hadoop.hive.ql.io.HiveOutputFormat";



  static class DatabaseBuilder {
    private String name, description, location;
    private Map<String, String> params = new HashMap<>();

    public DatabaseBuilder() {
    }

    static DatabaseBuilder get() {
      return new DatabaseBuilder();
    }

    DatabaseBuilder setName(String name) {
      this.name = name;
      return this;
    }

    DatabaseBuilder setDescription(String description) {
      this.description = description;
      return this;
    }

    DatabaseBuilder setLocation(String location) {
      this.location = location;
      return this;
    }

    DatabaseBuilder setParams(Map<String, String> params) {
      this.params = params;
      return this;
    }

    Database build() throws TException {
      assert name != null : "name must be set";
      return new Database(name, description, location, params);
    }
  }

  // This class does not have setters because it would return the wrong type
  private static class StorageDescriptorBuilder {
    protected String location, inputFormat, outputFormat, serdeName, serdeLib;
    protected List<FieldSchema> cols;
    protected int numBuckets;
    protected Map<String, String> storageDescriptorParams, serdeParams;
    protected boolean compressed, storedAsSubDirectories;
    protected List<String> bucketCols, skewedColNames;
    protected List<Order> sortCols;
    protected List<List<String>> skewedColValues;
    protected Map<List<String>,String> skewedColValueLocationMaps;

    protected StorageDescriptorBuilder() {
      // Set some reasonable defaults
      storageDescriptorParams = new HashMap<>();
      serdeParams = new HashMap<>();
      bucketCols = new ArrayList<>();
      sortCols = new ArrayList<>();
      numBuckets = 0;
      compressed = false;
      inputFormat = INPUT_FORMAT;
      outputFormat = OUTPUT_FORMAT;
      serdeLib = SERDE_LIB;
      skewedColNames = new ArrayList<>();
      skewedColValues = new ArrayList<>();
      skewedColValueLocationMaps = new HashMap<>();
    }

    protected StorageDescriptor buildSd() {
      assert cols != null : "cols must be set";
      SerDeInfo serdeInfo = new SerDeInfo(serdeName, serdeLib, serdeParams);
      StorageDescriptor sd = new StorageDescriptor(cols, location, inputFormat, outputFormat,
          compressed, numBuckets, serdeInfo, bucketCols, sortCols, storageDescriptorParams);
      sd.setStoredAsSubDirectories(storedAsSubDirectories);
      SkewedInfo skewed = new SkewedInfo(skewedColNames, skewedColValues,
          skewedColValueLocationMaps);
      sd.setSkewedInfo(skewed);
      return sd;
    }
  }

  static class TableBuilder extends StorageDescriptorBuilder {
    private String dbName, tableName, owner, viewOriginalText, viewExpandedText, type;
    private List<FieldSchema> partCols;
    private int createTime, lastAccessTime, retention;
    private Map<String, String> tableParams;

    private TableBuilder() {
      // Set some reasonable defaults
      tableParams = new HashMap<>();
      owner = "me";
      createTime = lastAccessTime = retention = 0;
    }

    static TableBuilder get() {
      return new TableBuilder();
    }

    TableBuilder setDbName(String dbName) {
      this.dbName = dbName;
      return this;
    }

    TableBuilder setTableName(String tableName) {
      this.tableName = tableName;
      return this;
    }

    TableBuilder setCols(List<FieldSchema> cols) {
      this.cols = cols;
      return this;
    }

    TableBuilder setLocation(String location) {
      this.location = location;
      return this;
    }

    TableBuilder setInputFormat(String inputFormat) {
      this.inputFormat = inputFormat;
      return this;
    }

    TableBuilder setOutputFormat(String outputFormat) {
      this.outputFormat = outputFormat;
      return this;
    }

    TableBuilder setSerdeName(String serdeName) {
      this.serdeName = serdeName;
      return this;
    }

    TableBuilder setSerdeLib(String serdeLib) {
      this.serdeLib = serdeLib;
      return this;
    }

    TableBuilder setOwner(String owner) {
      this.owner = owner;
      return this;
    }

    TableBuilder setViewOriginalText(String viewOriginalText) {
      this.viewOriginalText = viewOriginalText;
      return this;
    }

    TableBuilder setViewExpandedText(String viewExpandedText) {
      this.viewExpandedText = viewExpandedText;
      return this;
    }

    TableBuilder setType(String type) {
      this.type = type;
      return this;
    }

    TableBuilder setPartCols(List<FieldSchema> partCols) {
      this.partCols = partCols;
      return this;
    }

    TableBuilder setCreateTime(int createTime) {
      this.createTime = createTime;
      return this;
    }

    TableBuilder setLastAccessTime(int lastAccessTime) {
      this.lastAccessTime = lastAccessTime;
      return this;
    }

    TableBuilder setRetention(int retention) {
      this.retention = retention;
      return this;
    }

    TableBuilder setNumBuckets(int numBuckets) {
      this.numBuckets = numBuckets;
      return this;
    }

    TableBuilder setTableParams(Map<String, String> tableParams) {
      this.tableParams = tableParams;
      return this;
    }

    TableBuilder setStorageDescriptorParams(
        Map<String, String> storageDescriptorParams) {
      this.storageDescriptorParams = storageDescriptorParams;
      return this;
    }

    TableBuilder setSerdeParams(Map<String, String> serdeParams) {
      this.serdeParams = serdeParams;
      return this;
    }

    TableBuilder setCompressed(boolean compressed) {
      this.compressed = compressed;
      return this;
    }

    TableBuilder setBucketCols(List<String> bucketCols) {
      this.bucketCols = bucketCols;
      return this;
    }

    TableBuilder setSortCols(List<Order> sortCols) {
      this.sortCols = sortCols;
      return this;
    }

    TableBuilder setSkewedColNames(List<String> skewedColNames) {
      this.skewedColNames = skewedColNames;
      return this;
    }

    TableBuilder setSkewedColsValues(List<List<String>> skewedColsValues) {
      this.skewedColValues = skewedColsValues;
      return this;

    }

    TableBuilder setSkewedColValueLocationMaps(Map<List<String>,String> skewedColValueLocationMaps) {
      this.skewedColValueLocationMaps = skewedColValueLocationMaps;
      return this;
    }

    Table build() {
      assert dbName != null : "dbName must be set";
      assert tableName != null : "tableName must be set";
      return new Table(tableName, dbName, owner, createTime, lastAccessTime, retention, buildSd(),
          partCols, tableParams, viewOriginalText, viewExpandedText, type);
    }
  }

  static class PartitionBuilder extends StorageDescriptorBuilder {
    private String dbName, tableName;
    private int createTime, lastAccessTime;
    private Map<String, String> partParams;
    private List<String> values;

    private PartitionBuilder() {
      // Set some reasonable defaults
      partParams = new HashMap<>();
      createTime = lastAccessTime = 0;
    }

    static PartitionBuilder get() {
      return new PartitionBuilder();
    }

    PartitionBuilder setDbName(String dbName) {
      this.dbName = dbName;
      return this;
    }

    PartitionBuilder setTableName(String tableName) {
      this.tableName = tableName;
      return this;
    }

    PartitionBuilder setValues(List<String> values) {
      this.values = values;
      return this;
    }

    PartitionBuilder setLocation(String location) {
      this.location = location;
      return this;
    }

    PartitionBuilder setInputFormat(String inputFormat) {
      this.inputFormat = inputFormat;
      return this;
    }

    PartitionBuilder setOutputFormat(String outputFormat) {
      this.outputFormat = outputFormat;
      return this;
    }

    PartitionBuilder setSerdeName(String serdeName) {
      this.serdeName = serdeName;
      return this;
    }

    PartitionBuilder setSerdeLib(String serdeLib) {
      this.serdeLib = serdeLib;
      return this;
    }

    PartitionBuilder setCols(List<FieldSchema> cols) {
      this.cols = cols;
      return this;
    }

    PartitionBuilder setCreateTime(int createTime) {
      this.createTime = createTime;
      return this;
    }

    PartitionBuilder setLastAccessTime(int lastAccessTime) {
      this.lastAccessTime = lastAccessTime;
      return this;
    }

    PartitionBuilder setNumBuckets(int numBuckets) {
      this.numBuckets = numBuckets;
      return this;
    }

    PartitionBuilder setStorageDescriptorParams(
        Map<String, String> storageDescriptorParams) {
      this.storageDescriptorParams = storageDescriptorParams;
      return this;
    }

    PartitionBuilder setSerdeParams(Map<String, String> serdeParams) {
      this.serdeParams = serdeParams;
      return this;
    }

    PartitionBuilder setCompressed(boolean compressed) {
      this.compressed = compressed;
      return this;
    }

    PartitionBuilder setBucketCols(List<String> bucketCols) {
      this.bucketCols = bucketCols;
      return this;
    }

    PartitionBuilder setPartParams(Map<String, String> partParams) {
      this.partParams = partParams;
      return this;
    }

    PartitionBuilder setSortCols(List<Order> sortCols) {
      this.sortCols = sortCols;
      return this;
    }

    PartitionBuilder setSkewedColNames(List<String> skewedColNames) {
      this.skewedColNames = skewedColNames;
      return this;
    }

    PartitionBuilder setSkewedColsValues(List<List<String>> skewedColsValues) {
      this.skewedColValues = skewedColsValues;
      return this;

    }

    PartitionBuilder setSkewedColValueLocationMaps(Map<List<String>,String> skewedColValueLocationMaps) {
      this.skewedColValueLocationMaps = skewedColValueLocationMaps;
      return this;
    }

    Partition build() {
      assert dbName != null : "dbName must be set";
      assert tableName != null : "tableName must be set";
      assert values != null : "values must be set";
      return new Partition(values, dbName, tableName, createTime, lastAccessTime, buildSd(),
          partParams);
    }
  }

  static class IndexBuilder extends StorageDescriptorBuilder {
    private String dbName, tableName, indexName, indexTableName, handlerClass;
    private int createTime, lastAccessTime;
    private Map<String, String> indexParams;
    private boolean deferredRebuild;

    private IndexBuilder() {
      // Set some reasonable defaults
      indexParams = new HashMap<>();
      createTime = lastAccessTime = 0;
    }

    static IndexBuilder get() {
      return new IndexBuilder();
    }

    IndexBuilder setDbName(String dbName) {
      this.dbName = dbName;
      return this;
    }

    IndexBuilder setTableName(String tableName) {
      this.tableName = tableName;
      return this;
    }

    IndexBuilder setLocation(String location) {
      this.location = location;
      return this;
    }

    IndexBuilder setInputFormat(String inputFormat) {
      this.inputFormat = inputFormat;
      return this;
    }

    IndexBuilder setOutputFormat(String outputFormat) {
      this.outputFormat = outputFormat;
      return this;
    }

    IndexBuilder setSerdeName(String serdeName) {
      this.serdeName = serdeName;
      return this;
    }

    IndexBuilder setSerdeLib(String serdeLib) {
      this.serdeLib = serdeLib;
      return this;
    }

    IndexBuilder setCols(List<FieldSchema> cols) {
      this.cols = cols;
      return this;
    }

    IndexBuilder setCreateTime(int createTime) {
      this.createTime = createTime;
      return this;
    }

    IndexBuilder setLastAccessTime(int lastAccessTime) {
      this.lastAccessTime = lastAccessTime;
      return this;
    }

    IndexBuilder setNumBuckets(int numBuckets) {
      this.numBuckets = numBuckets;
      return this;
    }

    IndexBuilder setIndexParams(Map<String, String> indexParams) {
      this.indexParams = indexParams;
      return this;
    }

    IndexBuilder setStorageDescriptorParams(
        Map<String, String> storageDescriptorParams) {
      this.storageDescriptorParams = storageDescriptorParams;
      return this;
    }

    IndexBuilder setSerdeParams(Map<String, String> serdeParams) {
      this.serdeParams = serdeParams;
      return this;
    }

    IndexBuilder setCompressed(boolean compressed) {
      this.compressed = compressed;
      return this;
    }

    IndexBuilder setBucketCols(List<String> bucketCols) {
      this.bucketCols = bucketCols;
      return this;
    }

    IndexBuilder setIndexName(String indexName) {
      this.indexName = indexName;
      return this;
    }

    IndexBuilder setIndexTableName(String indexTableName) {
      this.indexTableName = indexTableName;
      return this;
    }

    IndexBuilder setHandlerClass(String handlerClass) {
      this.handlerClass = handlerClass;
      return this;
    }

    IndexBuilder setDeferredRebuild(boolean deferredRebuild) {
      this.deferredRebuild = deferredRebuild;
      return this;
    }

    IndexBuilder setSortCols(List<Order> sortCols) {
      this.sortCols = sortCols;
      return this;
    }

    IndexBuilder setSkewedColNames(List<String> skewedColNames) {
      this.skewedColNames = skewedColNames;
      return this;
    }

    IndexBuilder setSkewedColsValues(List<List<String>> skewedColsValues) {
      this.skewedColValues = skewedColsValues;
      return this;

    }

    IndexBuilder setSkewedColValueLocationMaps(Map<List<String>,String> skewedColValueLocationMaps) {
      this.skewedColValueLocationMaps = skewedColValueLocationMaps;
      return this;
    }

    Index build() {
      assert dbName != null : "dbName must be set";
      assert tableName != null : "tableName must be set";
      assert indexName != null : "indexName must be set";
      assert indexTableName != null : "indexTableName must be set";
      return new Index(indexName, handlerClass, dbName, tableName, createTime, lastAccessTime,
          indexTableName, buildSd(), indexParams, deferredRebuild);
    }
  }
}
