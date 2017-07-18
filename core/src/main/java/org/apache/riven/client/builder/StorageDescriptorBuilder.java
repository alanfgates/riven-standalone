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
import org.apache.riven.api.SerDeInfo;
import org.apache.riven.api.SkewedInfo;
import org.apache.riven.api.StorageDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Having this  extend PrincipalPrivilegeSetBuilder isn't perfect as somethings (indexes) have
// storage descriptors and not privilege sets.  But it's better than copying the code everywhere.
abstract class StorageDescriptorBuilder<T> extends PrincipalPrivilegeSetBuilder<T> {
  private String location, inputFormat, outputFormat, serdeName, serdeLib;
  private List<FieldSchema> cols;
  private int numBuckets;
  private Map<String, String> storageDescriptorParams, serdeParams;
  private boolean compressed, storedAsSubDirectories;
  private List<String> bucketCols, skewedColNames;
  private List<Order> sortCols;
  private List<List<String>> skewedColValues;
  private Map<List<String>, String> skewedColValueLocationMaps;
  // This enables us to return the correct type from the builder
  private T child;

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
    if (skewedColNames != null) {
      SkewedInfo skewed = new SkewedInfo(skewedColNames, skewedColValues,
          skewedColValueLocationMaps);
      sd.setSkewedInfo(skewed);
    }
    return sd;
  }

  protected void setChild(T child) {
    this.child = child;
    super.setChild(child);
  }

  public T setLocation(String location) {
    this.location = location;
    return child;
  }

  public T setInputFormat(String inputFormat) {
    this.inputFormat = inputFormat;
    return child;
  }

  public T setOutputFormat(String outputFormat) {
    this.outputFormat = outputFormat;
    return child;
  }

  public T setSerdeName(String serdeName) {
    this.serdeName = serdeName;
    return child;
  }

  public T setSerdeLib(String serdeLib) {
    this.serdeLib = serdeLib;
    return child;
  }

  public T setCols(List<FieldSchema> cols) {
    this.cols = cols;
    return child;
  }

  public T addCol(String name, String type, String comment) {
    if (cols == null) cols = new ArrayList<>();
    cols.add(new FieldSchema(name, type, comment));
    return child;
  }

  public T addCol(String name, String type) {
    return addCol(name, type, "");
  }

  public T setNumBuckets(int numBuckets) {
    this.numBuckets = numBuckets;
    return child;
  }

  public T setStorageDescriptorParams(
      Map<String, String> storageDescriptorParams) {
    this.storageDescriptorParams = storageDescriptorParams;
    return child;
  }

  public T addStorageDescriptorParam(String key, String value) {
    if (storageDescriptorParams == null) storageDescriptorParams = new HashMap<>();
    storageDescriptorParams.put(key, value);
    return child;
  }

  public T setSerdeParams(Map<String, String> serdeParams) {
    this.serdeParams = serdeParams;
    return child;
  }

  public T addSerdeParam(String key, String value) {
    if (serdeParams == null) serdeParams = new HashMap<>();
    serdeParams.put(key, value);
    return child;
  }

  public T setCompressed(boolean compressed) {
    this.compressed = compressed;
    return child;
  }

  public T setStoredAsSubDirectories(boolean storedAsSubDirectories) {
    this.storedAsSubDirectories = storedAsSubDirectories;
    return child;
  }

  public T setBucketCols(List<String> bucketCols) {
    this.bucketCols = bucketCols;
    return child;
  }

  public T addBucketCol(String bucketCol) {
    if (bucketCols == null) bucketCols = new ArrayList<>();
    bucketCols.add(bucketCol);
    return child;
  }

  public T setSkewedColNames(List<String> skewedColNames) {
    this.skewedColNames = skewedColNames;
    return child;
  }

  public T addSkewedColName(String skewedColName) {
    if (skewedColNames == null) skewedColNames = new ArrayList<>();
    skewedColNames.add(skewedColName);
    return child;
  }

  public T setSortCols(List<Order> sortCols) {
    this.sortCols = sortCols;
    return child;
  }

  public T addSortCol(String col, int order) {
    if (sortCols == null) sortCols = new ArrayList<>();
    sortCols.add(new Order(col, order));
    return child;
  }

  // It is not at all clear how to flatten these last two out in a useful way, and no one uses
  // these anyway.
  public T setSkewedColValues(List<List<String>> skewedColValues) {
    this.skewedColValues = skewedColValues;
    return child;
  }

  public T setSkewedColValueLocationMaps(
      Map<List<String>, String> skewedColValueLocationMaps) {
    this.skewedColValueLocationMaps = skewedColValueLocationMaps;
    return child;
  }
}
