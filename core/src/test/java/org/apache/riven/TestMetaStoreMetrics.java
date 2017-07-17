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
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.metrics.Metrics;
import org.apache.riven.metrics.MetricsConstants;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Tests Hive Metastore Metrics.
 *
 */
public class TestMetaStoreMetrics {

  static final private Logger LOG = LoggerFactory.getLogger(TestMetaStoreMetrics.class);

  private static Configuration conf;
  private static MetaStoreClient msc;

  @BeforeClass
  public static void before() throws Exception {
    int port = MetaStoreUtils.findFreePort();

    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setBoolVar(conf, ConfVars.METRICS_ENABLED, true);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);

    //Increments one HMS connection
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge(), conf);
    msc = new MetaStoreClient(conf);
  }


  @Test
  public void testMethodCounts() throws Exception {
    msc.getAllDatabases();

    Assert.assertEquals(2, Metrics.getRegistry().getTimers().get("api_get_all_databases").getCount());
  }

  @Test
  public void testMetaDataCounts() throws Exception {
    int initDbCount =
        (Integer)Metrics.getRegistry().getGauges().get(MetricsConstants.TOTAL_DATABASES).getValue();
    int initTblCount =
        (Integer)Metrics.getRegistry().getGauges().get(MetricsConstants.TOTAL_TABLES).getValue();
    int initPartCount =
        (Integer)Metrics.getRegistry().getGauges().get(MetricsConstants.TOTAL_PARTITIONS).getValue();

    //1 databases created
    String db1 = "testdb1";
    msc.createDatabase(UtilsForTests.DatabaseBuilder.get()
        .setName(db1)
        .build()
    );

    //4 tables
    String table1 = "testtbl1", table2 = "testtbl2", parttbl3 = "testtblpart",
        parttbl4 = "testtblpart2";
    List<FieldSchema> cols = Collections.singletonList(new FieldSchema("key", "string", ""));
    List<FieldSchema> partCols = Collections.singletonList(new FieldSchema("partkey", "string", ""));

    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName("default")
        .setTableName(table1)
        .setCols(cols)
        .build()
    );
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName("default")
        .setTableName(parttbl3)
        .setCols(cols)
        .setPartCols(partCols)
        .build()
    );
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName(db1)
        .setTableName(table2)
        .setCols(cols)
        .build()
    );
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName(db1)
        .setTableName(parttbl4)
        .setCols(cols)
        .setPartCols(partCols)
        .build()
    );

    //6 partitions
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName("default")
        .setTableName(parttbl3)
        .setCols(cols)
        .setValues(Collections.singletonList("a"))
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName("default")
        .setTableName(parttbl3)
        .setCols(cols)
        .setValues(Collections.singletonList("b"))
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName("default")
        .setTableName(parttbl3)
        .setCols(cols)
        .setValues(Collections.singletonList("c"))
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(db1)
        .setTableName(parttbl4)
        .setCols(cols)
        .setValues(Collections.singletonList("a"))
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(db1)
        .setTableName(parttbl4)
        .setCols(cols)
        .setValues(Collections.singletonList("b"))
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(db1)
        .setTableName(parttbl4)
        .setCols(cols)
        .setValues(Collections.singletonList("c"))
        .build()
    );

    //create and drop some additional metadata, to test drop counts.
    String tmpDb = "tempdb";
    msc.createDatabase(UtilsForTests.DatabaseBuilder.get()
        .setName(tmpDb)
        .build()
    );

    String delTable = "delete_by_table";
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName(tmpDb)
        .setTableName(delTable)
        .setCols(cols)
        .setPartCols(partCols)
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(tmpDb)
        .setTableName(delTable)
        .setCols(cols)
        .setValues(Collections.singletonList("temp"))
        .build()
    );
    msc.dropTable(tmpDb, delTable);

    delTable = "delete_by_part";
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName(tmpDb)
        .setTableName(delTable)
        .setCols(cols)
        .setPartCols(partCols)
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(tmpDb)
        .setTableName(delTable)
        .setCols(cols)
        .setValues(Collections.singletonList("temp"))
        .build()
    );
    msc.dropPartition(tmpDb, delTable, Collections.singletonList("temp"));

    delTable = "drop_by_db";
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName(tmpDb)
        .setTableName(delTable)
        .setCols(cols)
        .setPartCols(partCols)
        .build()
    );
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(tmpDb)
        .setTableName(delTable)
        .setCols(cols)
        .setValues(Collections.singletonList("temp"))
        .build()
    );
    msc.dropDatabase(tmpDb, true, true, true);

    Assert.assertEquals(2, Metrics.getRegistry().getCounters().get(MetricsConstants.CREATE_TOTAL_DATABASES).getCount());
    Assert.assertEquals(7, Metrics.getRegistry().getCounters().get(MetricsConstants.CREATE_TOTAL_TABLES).getCount());
    Assert.assertEquals(9, Metrics.getRegistry().getCounters().get(MetricsConstants.CREATE_TOTAL_PARTITIONS).getCount());

    Assert.assertEquals(1, Metrics.getRegistry().getCounters().get(MetricsConstants.DELETE_TOTAL_DATABASES).getCount());
    Assert.assertEquals(3, Metrics.getRegistry().getCounters().get(MetricsConstants.DELETE_TOTAL_TABLES).getCount());
    Assert.assertEquals(3, Metrics.getRegistry().getCounters().get(MetricsConstants.DELETE_TOTAL_PARTITIONS).getCount());

    //to test initial metadata count metrics.
    Assert.assertEquals(initDbCount + 1,
        Metrics.getRegistry().getGauges().get(MetricsConstants.TOTAL_DATABASES).getValue());
    Assert.assertEquals(initTblCount + 4,
        Metrics.getRegistry().getGauges().get(MetricsConstants.TOTAL_TABLES).getValue());
    Assert.assertEquals(initPartCount + 6,
        Metrics.getRegistry().getGauges().get(MetricsConstants.TOTAL_PARTITIONS).getValue());
  }


  @Test
  public void testConnections() throws Exception {

    //initial state is one connection
    int initialCount =
        (Integer)Metrics.getRegistry().getGauges().get(MetricsConstants.OPEN_CONNECTIONS).getValue();

    //create two connections
    MetaStoreClient msc = new MetaStoreClient(conf);
    MetaStoreClient msc2 = new MetaStoreClient(conf);

    Assert.assertEquals(initialCount + 2,
        Metrics.getRegistry().getGauges().get(MetricsConstants.OPEN_CONNECTIONS).getValue());

    //close one connection, verify still two left
    msc.close();
    Thread.sleep(500);
    Assert.assertEquals(initialCount + 1,
        Metrics.getRegistry().getGauges().get(MetricsConstants.OPEN_CONNECTIONS).getValue());

    //close one connection, verify still one left
    msc2.close();
    Thread.sleep(500);
    Assert.assertEquals(initialCount,
        Metrics.getRegistry().getGauges().get(MetricsConstants.OPEN_CONNECTIONS).getValue());
  }
}
