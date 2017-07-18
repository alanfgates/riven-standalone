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
import org.apache.riven.api.ColumnStatistics;
import org.apache.riven.api.ColumnStatisticsData;
import org.apache.riven.api.ColumnStatisticsDesc;
import org.apache.riven.api.ColumnStatisticsObj;
import org.apache.riven.api.Database;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.SerDeInfo;
import org.apache.riven.api.SetPartitionsStatsRequest;
import org.apache.riven.api.StorageDescriptor;
import org.apache.riven.api.StringColumnStatsData;
import org.apache.riven.api.Table;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.client.builder.DatabaseBuilder;
import org.apache.riven.client.builder.TableBuilder;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.listeners.events.CreateDatabaseEvent;
import org.apache.riven.listeners.events.CreateTableEvent;
import org.apache.riven.listeners.events.ListenerEvent;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetastoreStatsMerge {

  private Configuration conf;
  private MetaStoreClient msc;
  private Database db;
  private Table table;

  private static final String dbName = "hive3253";
  private static final String tblName = "tmptbl";

  @Before
  public void setUp() throws Exception {
    System.setProperty("metastore.event.listeners",
        DummyListener.class.getName());

    int port = MetaStoreUtils.findFreePort();
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge());

    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    msc = new MetaStoreClient(conf);

    msc.dropDatabase(dbName, true, true);

    db = new DatabaseBuilder()
        .setName(dbName)
        .build();

    Map<String, String> tableParams = new HashMap<>();
    tableParams.put("a", "string");

    List<FieldSchema> cols = new ArrayList<>();
    cols.add(new FieldSchema("a", "string", ""));

    table = new TableBuilder()
        .setDbName(dbName)
        .setTableName(tblName)
        .setTableParams(tableParams)
        .setCols(cols)
        .setStorageDescriptorParams(tableParams)
        .setSerdeName(tblName)
        .build();

    DummyListener.notifyList.clear();
  }

  @Test
  public void testStatsMerge() throws Exception {
    int listSize = 0;

    List<ListenerEvent> notifyList = DummyListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);
    msc.createDatabase(db);
    listSize++;
    Assert.assertEquals(listSize, notifyList.size());
    CreateDatabaseEvent dbEvent = (CreateDatabaseEvent)(notifyList.get(listSize - 1));
    assert dbEvent.getStatus();

    msc.createTable(table);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    CreateTableEvent tblEvent = (CreateTableEvent)(notifyList.get(listSize - 1));
    assert tblEvent.getStatus();

    table = msc.getTable(dbName, tblName);

    ColumnStatistics cs = new ColumnStatistics();
    ColumnStatisticsDesc desc = new ColumnStatisticsDesc(true, dbName, tblName);
    cs.setStatsDesc(desc);
    ColumnStatisticsObj obj = new ColumnStatisticsObj();
    obj.setColName("a");
    obj.setColType("string");
    ColumnStatisticsData data = new ColumnStatisticsData();
    StringColumnStatsData scsd = new StringColumnStatsData();
    scsd.setAvgColLen(10);
    scsd.setMaxColLen(20);
    scsd.setNumNulls(30);
    scsd.setNumDVs(123);
    scsd.setBitVectors("{0, 4, 5, 7}{0, 1}{0, 1, 2}{0, 1, 4}{0}{0, 2}{0, 3}{0, 2, 3, 4}{0, 1, 4}{0, 1}{0}{0, 1, 3, 8}{0, 2}{0, 2}{0, 9}{0, 1, 4}");
    data.setStringStats(scsd);
    obj.setStatsData(data);
    cs.addToStatsObj(obj);
    
    List<ColumnStatistics> colStats = new ArrayList<>();
    colStats.add(cs);
    
    SetPartitionsStatsRequest request = new SetPartitionsStatsRequest(colStats);
    msc.setPartitionColumnStatistics(request);

    List<String> colNames = new ArrayList<>();
    colNames.add("a");

    StringColumnStatsData getScsd = msc.getTableColumnStatistics(dbName, tblName, colNames).get(0)
        .getStatsData().getStringStats();
    Assert.assertEquals(getScsd.getNumDVs(), 123);
    
    cs = new ColumnStatistics();
    scsd = new StringColumnStatsData();
    scsd.setAvgColLen(20);
    scsd.setMaxColLen(5);
    scsd.setNumNulls(70);
    scsd.setNumDVs(456);
    scsd.setBitVectors("{0, 1}{0, 1}{1, 2, 4}{0, 1, 2}{0, 1, 2}{0, 2}{0, 1, 3, 4}{0, 1}{0, 1}{3, 4, 6}{2}{0, 1}{0, 3}{0}{0, 1}{0, 1, 4}");
    data.setStringStats(scsd);
    obj.setStatsData(data);
    cs.addToStatsObj(obj);
    
    request = new SetPartitionsStatsRequest(colStats);
    request.setNeedMerge(true);
    msc.setPartitionColumnStatistics(request);
    
    getScsd = msc.getTableColumnStatistics(dbName, tblName, colNames).get(0)
        .getStatsData().getStringStats();
    Assert.assertEquals(getScsd.getAvgColLen(), 20.0, 0.01);
    Assert.assertEquals(getScsd.getMaxColLen(), 20);
    Assert.assertEquals(getScsd.getNumNulls(), 100);
    // since metastore is ObjectStore, we use the max function to merge.
    Assert.assertEquals(getScsd.getNumDVs(), 456);

  }

}
