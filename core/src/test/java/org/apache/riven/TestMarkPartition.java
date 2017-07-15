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
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.InvalidPartitionException;
import org.apache.riven.api.PartitionEventType;
import org.apache.riven.api.UnknownTableException;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.conf.MetastoreConf;
import org.apache.thrift.TException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMarkPartition {

  protected Configuration conf;
  private MetaStoreClient msc;
  private final String dbName = "hive2215";

  @Before
  public void setUp() throws Exception {
    System.setProperty("metastore.event.clean.freq", "2");
    System.setProperty("metastore.event.expiry.duration", "5");
    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setBoolVar(conf, MetastoreConf.ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
  }

  @Test
  public void testMarkingPartitionSet() throws TException, InterruptedException {

    msc = new MetaStoreClient(conf);
    msc.createDatabase(UtilsForTests.DatabaseBuilder.get()
        .setName(dbName)
        .build()
    );
    String tableName = "tmptbl";
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName(dbName)
        .setTableName(tableName)
        .setCols(Collections.singletonList(new FieldSchema("a", "string", "")))
        .setPartCols(Collections.singletonList(new FieldSchema("b", "string", "")))
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(dbName)
        .setTableName(tableName)
        .setValues(Collections.singletonList("2011"))
        .setCols(Collections.singletonList(new FieldSchema("a", "string", "")))
        .build()
    );

    Map<String,String> kvs = new HashMap<>();
    kvs.put("b", "'2011'");
    msc.markPartitionForEvent(dbName, tableName, kvs, PartitionEventType.LOAD_DONE);
    Assert.assertTrue(msc.isPartitionMarkedForEvent(dbName, tableName, kvs,
        PartitionEventType.LOAD_DONE));
    Thread.sleep(10000);
    Assert.assertFalse(msc.isPartitionMarkedForEvent(dbName, tableName, kvs,
        PartitionEventType.LOAD_DONE));

    kvs.put("b", "'2012'");
    Assert.assertFalse(msc.isPartitionMarkedForEvent(dbName, tableName, kvs,
        PartitionEventType.LOAD_DONE));
    String bogusTableName = "tmptbl2";
    try{
      msc.markPartitionForEvent(dbName, bogusTableName, kvs, PartitionEventType.LOAD_DONE);
      Assert.fail();
    } catch(Exception e){
      Assert.assertTrue(e instanceof UnknownTableException);
    }
    try{
      msc.isPartitionMarkedForEvent(dbName, bogusTableName, kvs, PartitionEventType.LOAD_DONE);
      Assert.fail();
    } catch(Exception e){
      Assert.assertTrue(e instanceof UnknownTableException);
    }
    kvs.put("a", "'2012'");
    try{
      msc.isPartitionMarkedForEvent(dbName, tableName, kvs, PartitionEventType.LOAD_DONE);
      Assert.fail();
    } catch(Exception e){
      Assert.assertTrue(e instanceof InvalidPartitionException);
    }
  }

  @After
  public void tearDown() throws Exception {
    if (msc != null) msc.dropDatabase(dbName, true, true, true);
  }

}
