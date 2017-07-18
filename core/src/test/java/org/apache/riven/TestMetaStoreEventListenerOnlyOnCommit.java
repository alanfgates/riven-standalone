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

import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.client.builder.DatabaseBuilder;
import org.apache.riven.client.builder.PartitionBuilder;
import org.apache.riven.client.builder.TableBuilder;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.listeners.events.ListenerEvent;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Ensure that the status of MetaStore events depend on the RawStore's commit status.
 */
public class TestMetaStoreEventListenerOnlyOnCommit {

  private Configuration conf;
  private MetaStoreClient msc;

  @Before
  public void setUp() throws Exception {

    DummyRawStoreControlledCommit.setCommitSucceed(true);

    System.setProperty(ConfVars.EVENT_LISTENERS.varname, DummyListener.class.getName());
    System.setProperty(ConfVars.RAW_STORE_IMPL.varname, DummyRawStoreControlledCommit.class.getName());

    int port = MetaStoreUtils.findFreePort();
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge());

    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    msc = new MetaStoreClient(conf);

    DummyListener.notifyList.clear();
  }

  @Test
  public void testEventStatus() throws Exception {
    int listSize = 0;
    List<ListenerEvent> notifyList = DummyListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);

    String dbName = "tmpDb";
    msc.createDatabase(new DatabaseBuilder()
        .setName(dbName)
        .build()
    );
    listSize += 1;
    notifyList = DummyListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);
    Assert.assertTrue(DummyListener.getLastEvent().getStatus());

    String tableName = "unittest_TestMetaStoreEventListenerOnlyOnCommit";
    List<FieldSchema> cols = Collections.singletonList(new FieldSchema("id", "int", ""));
    msc.createTable(new TableBuilder()
        .setDbName(dbName)
        .setTableName(tableName)
        .setCols(cols)
        .setPartCols(Collections.singletonList(new FieldSchema("ds", "string", "")))
        .build()
    );
    listSize += 1;
    notifyList = DummyListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);
    Assert.assertTrue(DummyListener.getLastEvent().getStatus());

    msc.add_partition(new PartitionBuilder()
        .setDbName(dbName)
        .setTableName(tableName)
        .setCols(cols)
        .setValues(Collections.singletonList("foo1"))
        .build()
    );
    listSize += 1;
    notifyList = DummyListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);
    Assert.assertTrue(DummyListener.getLastEvent().getStatus());

    DummyRawStoreControlledCommit.setCommitSucceed(false);

    msc.add_partition(new PartitionBuilder()
        .setDbName(dbName)
        .setTableName(tableName)
        .setCols(cols)
        .setValues(Collections.singletonList("foo2"))
        .build()
    );
    listSize += 1;
    notifyList = DummyListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);
    Assert.assertFalse(DummyListener.getLastEvent().getStatus());

  }
}
