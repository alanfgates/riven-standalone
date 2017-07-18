/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.riven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.Database;
import org.apache.riven.api.EnvironmentContext;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.Partition;
import org.apache.riven.api.SerDeInfo;
import org.apache.riven.api.StorageDescriptor;
import org.apache.riven.api.Table;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.client.builder.DatabaseBuilder;
import org.apache.riven.client.builder.PartitionBuilder;
import org.apache.riven.client.builder.TableBuilder;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.listeners.events.AddPartitionEvent;
import org.apache.riven.listeners.events.AlterTableEvent;
import org.apache.riven.listeners.events.CreateDatabaseEvent;
import org.apache.riven.listeners.events.CreateTableEvent;
import org.apache.riven.listeners.events.DropDatabaseEvent;
import org.apache.riven.listeners.events.DropPartitionEvent;
import org.apache.riven.listeners.events.DropTableEvent;
import org.apache.riven.listeners.events.ListenerEvent;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TestHiveMetaStoreWithEnvironmentContext. Test case for _with_environment_context
 * calls in {@link org.apache.riven.impl.MetaStoreServer}
 */
public class TestMetastoreWithEnvironmentContext {

  private Configuration conf;
  private MetaStoreClient msc;
  private EnvironmentContext envContext;
  private Database db;
  private Table table;
  private Partition partition;

  private static final String dbName = "hive3252";
  private static final String tblName = "tmptbl";
  private static final String renamed = "tmptbl2";

  @Before
  public void setUp() throws Exception {
    System.setProperty("metastore.event.listeners", DummyListener.class.getName());

    int port = MetaStoreUtils.findFreePort();
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge());

    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    msc = new MetaStoreClient(conf);

    msc.dropDatabase(dbName, true, true);

    Map<String, String> envProperties = new HashMap<>();
    envProperties.put("hadoop.job.ugi", "test_user");
    envContext = new EnvironmentContext(envProperties);

    db = new DatabaseBuilder()
        .setName(dbName)
        .build();

    Map<String, String> tableParams = new HashMap<>();
    tableParams.put("a", "string");
    List<FieldSchema> partitionKeys = new ArrayList<>();
    partitionKeys.add(new FieldSchema("b", "string", ""));

    List<FieldSchema> cols = new ArrayList<>();
    cols.add(new FieldSchema("a", "string", ""));
    cols.add(new FieldSchema("b", "string", ""));
    table = new TableBuilder()
        .setDbName(dbName)
        .setTableName(tblName)
        .setTableParams(tableParams)
        .setPartCols(partitionKeys)
        .setCols(cols)
        .setStorageDescriptorParams(tableParams)
        .setSerdeName(tblName)
        .build();

    List<String> partValues = new ArrayList<>();
    partValues.add("2011");

    partition = new PartitionBuilder()
        .setDbName(dbName)
        .setTableName(tblName)
        .setValues(partValues)
        .setCols(cols)
        .setStorageDescriptorParams(tableParams)
        .setSerdeName(tblName)
        .build();

    DummyListener.notifyList.clear();
  }

  @Test
  public void testEnvironmentContext() throws Exception {
    int listSize = 0;

    List<ListenerEvent> notifyList = DummyListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);
    msc.createDatabase(db);
    listSize++;
    Assert.assertEquals(listSize, notifyList.size());
    CreateDatabaseEvent dbEvent = (CreateDatabaseEvent)(notifyList.get(listSize - 1));
    assert dbEvent.getStatus();

    msc.createTable(table, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    CreateTableEvent tblEvent = (CreateTableEvent)(notifyList.get(listSize - 1));
    assert tblEvent.getStatus();
    Assert.assertEquals(envContext, tblEvent.getEnvironmentContext());

    table = msc.getTable(dbName, tblName);

    partition.getSd().setLocation(table.getSd().getLocation() + "/part1");
    msc.add_partition(partition, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    AddPartitionEvent partEvent = (AddPartitionEvent)(notifyList.get(listSize-1));
    assert partEvent.getStatus();
    Assert.assertEquals(envContext, partEvent.getEnvironmentContext());

    List<String> partVals = new ArrayList<>();
    partVals.add("2012");
    msc.appendPartition(dbName, tblName, partVals, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    AddPartitionEvent appendPartEvent = (AddPartitionEvent)(notifyList.get(listSize-1));
    assert appendPartEvent.getStatus();
    Assert.assertEquals(envContext, appendPartEvent.getEnvironmentContext());

    table.setTableName(renamed);
    msc.alter_table_with_environmentContext(dbName, tblName, table, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    AlterTableEvent alterTableEvent = (AlterTableEvent) notifyList.get(listSize-1);
    assert alterTableEvent.getStatus();
    Assert.assertEquals(envContext, alterTableEvent.getEnvironmentContext());

    table.setTableName(tblName);
    msc.alter_table_with_environmentContext(dbName, renamed, table, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);

    List<String> dropPartVals = new ArrayList<>();
    dropPartVals.add("2011");
    msc.dropPartition(dbName, tblName, dropPartVals, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    DropPartitionEvent dropPartEvent = (DropPartitionEvent)notifyList.get(listSize - 1);
    assert dropPartEvent.getStatus();
    Assert.assertEquals(envContext, dropPartEvent.getEnvironmentContext());

    msc.dropPartition(dbName, tblName, "b=2012", true, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    DropPartitionEvent dropPartByNameEvent = (DropPartitionEvent)notifyList.get(listSize - 1);
    assert dropPartByNameEvent.getStatus();
    Assert.assertEquals(envContext, dropPartByNameEvent.getEnvironmentContext());

    msc.dropTable(dbName, tblName, true, false, envContext);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    DropTableEvent dropTblEvent = (DropTableEvent)notifyList.get(listSize-1);
    assert dropTblEvent.getStatus();
    Assert.assertEquals(envContext, dropTblEvent.getEnvironmentContext());

    msc.dropDatabase(dbName);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);

    DropDatabaseEvent dropDB = (DropDatabaseEvent)notifyList.get(listSize-1);
    assert dropDB.getStatus();
  }

}
