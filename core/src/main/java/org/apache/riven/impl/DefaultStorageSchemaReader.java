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
package org.apache.riven.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.StorageSchemaReader;
import org.apache.riven.api.EnvironmentContext;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.MetaException;
import org.apache.riven.api.Table;

import java.util.List;

/**
 * Default StorageSchemaReader.  This just throws as the metastore currently doesn't know how to
 * read schemas from storage.
 */
public class DefaultStorageSchemaReader implements StorageSchemaReader {
  @Override
  public List<FieldSchema> readSchema(Table tbl, EnvironmentContext envContext,
                                      Configuration conf) throws MetaException {
    throw new UnsupportedOperationException("Storage schema reading not supported");
  }
}
