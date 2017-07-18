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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.client.builder.DatabaseBuilder;
import org.apache.riven.client.builder.TableBuilder;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.impl.RetryingHMSHandler;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TestRetryingHMSHandler. Test case for
 * {@link RetryingHMSHandler}
 */
public class TestRetryingHMSHandler {
  private Configuration conf;
  private MetaStoreClient msc;

  @Before
  public void setUp() throws Exception {

    System.setProperty("metastore.pre.event.listeners",
        AlternateFailurePreListener.class.getName());
    int port = MetaStoreUtils.findFreePort();
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge());
    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setLongVar(conf, ConfVars.HMSHANDLERATTEMPTS, 2);
    MetastoreConf.setTimeVar(conf, ConfVars.HMSHANDLERINTERVAL, 0, TimeUnit.MILLISECONDS);
    MetastoreConf.setBoolVar(conf, ConfVars.HMSHANDLERFORCERELOADCONF, false);
    msc = new MetaStoreClient(conf);
  }

  // Create a database and a table in that database.  Because the AlternateFailurePreListener is
  // being used each attempt to create something should require two calls by the RetryingHMSHandler
  @Test
  public void testRetryingHMSHandler() throws Exception {
    String dbName = "hive4159";
    String tblName = "tmptbl";

    msc.createDatabase(new DatabaseBuilder()
        .setName(dbName)
        .build());

    Assert.assertEquals(2, AlternateFailurePreListener.getCallCount());

    List<FieldSchema> cols = Arrays.asList(
        new FieldSchema("c1", "string", ""),
        new FieldSchema("c2", "int", "")
    );

    Map<String, String> params = new HashMap<>();
    params.put("test_param_1", "Use this for comments etc");

    Map<String, String> serdParams = new HashMap<>();
    serdParams.put(ColumnType.SERIALIZATION_FORMAT, "1");


    msc.createTable(new TableBuilder()
        .setDbName(dbName)
        .setTableName(tblName)
        .setCols(cols)
        .setTableParams(params)
        .setSerdeParams(serdParams)
        .setNumBuckets(1)
        .setBucketCols(Collections.singletonList("name"))
        .build()
    );

    Assert.assertEquals(4, AlternateFailurePreListener.getCallCount());
  }

}
