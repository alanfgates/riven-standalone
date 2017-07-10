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

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.AlreadyExistsException;
import org.apache.riven.api.EnvironmentContext;
import org.apache.riven.api.InvalidObjectException;
import org.apache.riven.api.InvalidOperationException;
import org.apache.riven.api.MetaException;
import org.apache.riven.api.Partition;
import org.apache.riven.api.Table;
import org.apache.riven.impl.IHMSHandler;
import org.apache.riven.impl.RawStore;
import org.apache.riven.impl.Warehouse;

import java.util.List;

public class MockAlterHandler implements AlterHandler {
  @Override
  public void alterTable(RawStore msdb, Warehouse wh, String dbname, String name, Table newTable,
                         EnvironmentContext envContext) throws InvalidOperationException,
      MetaException {

  }

  @Override
  public void alterTable(RawStore msdb, Warehouse wh, String dbname, String name, Table newTable,
                         EnvironmentContext envContext, IHMSHandler handler) throws
      InvalidOperationException, MetaException {

  }

  @Override
  public Partition alterPartition(RawStore msdb, Warehouse wh, String dbname,
                                  String name, List<String> part_vals,
                                  Partition new_part,
                                  EnvironmentContext environmentContext) throws
      InvalidOperationException, InvalidObjectException, AlreadyExistsException, MetaException {
    return null;
  }

  @Override
  public Partition alterPartition(RawStore msdb, Warehouse wh, String dbname,
                                  String name, List<String> part_vals,
                                  Partition new_part, EnvironmentContext environmentContext,
                                  IHMSHandler handler) throws InvalidOperationException,
      InvalidObjectException, AlreadyExistsException, MetaException {
    return null;
  }

  @Override
  public List<Partition> alterPartitions(RawStore msdb, Warehouse wh, String dbname,
                                         String name, List<Partition> new_parts,
                                         EnvironmentContext environmentContext) throws
      InvalidOperationException, InvalidObjectException, AlreadyExistsException, MetaException {
    return null;
  }

  @Override
  public List<Partition> alterPartitions(RawStore msdb, Warehouse wh, String dbname,
                                         String name, List<Partition> new_parts,
                                         EnvironmentContext environmentContext,
                                         IHMSHandler handler) throws InvalidOperationException,
      InvalidObjectException, AlreadyExistsException, MetaException {
    return null;
  }

  @Override
  public void setConf(Configuration configuration) {

  }

  @Override
  public Configuration getConf() {
    return null;
  }
}
