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



import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.NoSuchObjectException;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.client.builder.DatabaseBuilder;
import org.apache.riven.client.builder.TableBuilder;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.impl.MetaStoreEndFunctionContext;
import org.apache.riven.impl.MetaStoreEndFunctionListener;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

/**
 * TestMetaStoreEventListener. Test case for
 * {@link MetaStoreEndFunctionListener}
 */
public class TestMetaStoreEndFunctionListener {
  private Configuration conf;
  private MetaStoreClient msc;

  @Before
  public void setUp() throws Exception {

    System.setProperty("metastore.event.listeners", DummyListener.class.getName());
    System.setProperty("metastore.pre.event.listeners",
        DummyPreListener.class.getName());
    System.setProperty("metastore.end.function.listeners",
        DummyEndFunctionListener.class.getName());
    int port = MetaStoreUtils.findFreePort();
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge());
    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    msc = new MetaStoreClient(conf);
  }

  @Test
  public void testEndFunctionListener() throws Exception {
    /* Objective here is to ensure that when exceptions are thrown in HiveMetaStore in API methods
     * they bubble up and are stored in the MetaStoreEndFunctionContext objects
     */
    String dbName = "hive3524";
    String tblName = "tmptbl";
    int listSize = 0;

    msc.createDatabase(new DatabaseBuilder()
        .setName(dbName)
        .build()
    );

    try {
      msc.getDatabase("UnknownDB");
    }
    catch (Exception e) {
    }
    listSize = DummyEndFunctionListener.funcNameList.size();
    String func_name = DummyEndFunctionListener.funcNameList.get(listSize-1);
    MetaStoreEndFunctionContext context = DummyEndFunctionListener.contextList.get(listSize-1);
    Assert.assertEquals(func_name,"get_database");
    Assert.assertFalse(context.isSuccess());
    Exception e = context.getException();
    Assert.assertTrue((e!=null));
    Assert.assertTrue((e instanceof NoSuchObjectException));
    Assert.assertEquals(context.getInputTableName(), null);

    msc.createTable(new TableBuilder()
        .setDbName(dbName)
        .setTableName(tblName)
        .setCols(Collections.singletonList(new FieldSchema("a", "string", "")))
        .setPartCols(Collections.singletonList(new FieldSchema("b", "string", "")))
        .build()
    );
    String tableName = "Unknown";
    try {
      msc.getTable(dbName, tableName);
    }
    catch (Exception e1) {
    }
    listSize = DummyEndFunctionListener.funcNameList.size();
    func_name = DummyEndFunctionListener.funcNameList.get(listSize-1);
    context = DummyEndFunctionListener.contextList.get(listSize-1);
    Assert.assertEquals(func_name,"get_table");
    Assert.assertFalse(context.isSuccess());
    e = context.getException();
    Assert.assertTrue((e!=null));
    Assert.assertTrue((e instanceof NoSuchObjectException));
    Assert.assertEquals(context.getInputTableName(), tableName);

    try {
      msc.getPartition("hive3524", tblName, "b=2012");
    }
    catch (Exception e2) {
    }
    listSize = DummyEndFunctionListener.funcNameList.size();
    func_name = DummyEndFunctionListener.funcNameList.get(listSize-1);
    context = DummyEndFunctionListener.contextList.get(listSize-1);
    Assert.assertEquals(func_name,"get_partition_by_name");
    Assert.assertFalse(context.isSuccess());
    e = context.getException();
    Assert.assertTrue((e!=null));
    Assert.assertTrue((e instanceof NoSuchObjectException));
    Assert.assertEquals(context.getInputTableName(), tblName);
    try {
      msc.dropTable(dbName, "Unknown");
    }
    catch (Exception e4) {
    }
    listSize = DummyEndFunctionListener.funcNameList.size();
    func_name = DummyEndFunctionListener.funcNameList.get(listSize-1);
    context = DummyEndFunctionListener.contextList.get(listSize-1);
    Assert.assertEquals(func_name,"get_table");
    Assert.assertFalse(context.isSuccess());
    e = context.getException();
    Assert.assertTrue((e!=null));
    Assert.assertTrue((e instanceof NoSuchObjectException));
    Assert.assertEquals(context.getInputTableName(), "Unknown");

  }

}
