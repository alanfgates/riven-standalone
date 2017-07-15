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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.Database;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.Index;
import org.apache.riven.api.MetaException;
import org.apache.riven.api.NoSuchObjectException;
import org.apache.riven.api.Partition;
import org.apache.riven.api.PartitionSpec;
import org.apache.riven.api.Table;
import org.apache.riven.client.DefaultMetaStoreFilterHookImpl;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestFilterHooks {

  public static class DummyMetaStoreFilterHookImpl extends DefaultMetaStoreFilterHookImpl {
    public static boolean blockResults = false;

    public DummyMetaStoreFilterHookImpl(Configuration conf) {
      super(conf);
    }

    @Override
    public List<String> filterDatabases(List<String> dbList) throws MetaException  {
      if (blockResults) {
        return new ArrayList<String>();
      }
      return super.filterDatabases(dbList);
    }

    @Override
    public Database filterDatabase(Database dataBase) throws NoSuchObjectException {
      if (blockResults) {
        throw new NoSuchObjectException("Blocked access");
      }
      return super.filterDatabase(dataBase);
    }

    @Override
    public List<String> filterTableNames(String dbName, List<String> tableList) throws MetaException {
      if (blockResults) {
        return new ArrayList<String>();
      }
      return super.filterTableNames(dbName, tableList);
    }

    @Override
    public Table filterTable(Table table) throws NoSuchObjectException {
      if (blockResults) {
        throw new NoSuchObjectException("Blocked access");
      }
      return super.filterTable(table);
    }

    @Override
    public List<Table> filterTables(List<Table> tableList) throws MetaException {
      if (blockResults) {
        return new ArrayList<Table>();
      }
      return super.filterTables(tableList);
    }

    @Override
    public List<Partition> filterPartitions(List<Partition> partitionList) throws MetaException {
      if (blockResults) {
        return new ArrayList<Partition>();
      }
      return super.filterPartitions(partitionList);
    }

    @Override
    public List<PartitionSpec> filterPartitionSpecs(
        List<PartitionSpec> partitionSpecList) throws MetaException {
      if (blockResults) {
        return new ArrayList<PartitionSpec>();
      }
      return super.filterPartitionSpecs(partitionSpecList);
    }

    @Override
    public Partition filterPartition(Partition partition) throws NoSuchObjectException {
      if (blockResults) {
        throw new NoSuchObjectException("Blocked access");
      }
      return super.filterPartition(partition);
    }

    @Override
    public List<String> filterPartitionNames(String dbName, String tblName,
        List<String> partitionNames) throws MetaException {
      if (blockResults) {
        return new ArrayList<String>();
      }
      return super.filterPartitionNames(dbName, tblName, partitionNames);
    }

    @Override
    public Index filterIndex(Index index) throws NoSuchObjectException {
      if (blockResults) {
        throw new NoSuchObjectException("Blocked access");
      }
      return super.filterIndex(index);
    }

    @Override
    public List<String> filterIndexNames(String dbName, String tblName,
        List<String> indexList) throws MetaException {
      if (blockResults) {
        return new ArrayList<String>();
      }
      return super.filterIndexNames(dbName, tblName, indexList);
    }

    @Override
    public List<Index> filterIndexes(List<Index> indexeList) throws MetaException {
      if (blockResults) {
        return new ArrayList<Index>();
      }
      return super.filterIndexes(indexeList);
    }
  }

  private static final String DBNAME1 = "testdb1";
  private static final String DBNAME2 = "testdb2";
  private static final String TAB1 = "tab1";
  private static final String TAB2 = "tab2";
  private static final String INDEX1 = "idx1";
  private static Configuration conf;
  private static MetaStoreClient msc;

  @BeforeClass
  public static void setUp() throws Exception {
    DummyMetaStoreFilterHookImpl.blockResults = false;

    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    MetastoreConf.setVar(conf, ConfVars.FILTER_HOOK, DummyMetaStoreFilterHookImpl.class.getName());
    //UtilsForTest.setNewDerbyDbLocation(conf, TestFilterHooks.class.getSimpleName());
    int port = MetaStoreUtils.findFreePort();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge(), conf);

    msc = new MetaStoreClient(conf);

    msc.createDatabase(UtilsForTests.DatabaseBuilder.get()
        .setName(DBNAME1)
        .build());
    msc.createDatabase(UtilsForTests.DatabaseBuilder.get()
        .setName(DBNAME2)
        .build());
    Table tab1 = UtilsForTests.TableBuilder.get()
        .setDbName(DBNAME1)
        .setTableName(TAB1)
        .setCols(Arrays.asList(new FieldSchema("id", "int", ""), new FieldSchema("name", "string", "")))
        .build();
    msc.createTable(tab1);
    msc.createTable(UtilsForTests.TableBuilder.get()
        .setDbName(DBNAME1)
        .setTableName(TAB2)
        .setCols(Collections.singletonList(new FieldSchema("id", "int", "")))
        .setPartCols(Collections.singletonList(new FieldSchema("name", "string", "")))
        .build());
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(DBNAME1)
        .setTableName(TAB2)
        .setValues(Collections.singletonList("value1"))
        .build());
    msc.add_partition(UtilsForTests.PartitionBuilder.get()
        .setDbName(DBNAME1)
        .setTableName(TAB2)
        .setValues(Collections.singletonList("value2"))
        .build());
    Table indexTable = UtilsForTests.TableBuilder.get()
        .setDbName(DBNAME1)
        .setTableName(INDEX1 + "_" + TAB1)
        .setCols(Collections.singletonList(new FieldSchema("id", "int", "")))
        .build();
    msc.createIndex(UtilsForTests.IndexBuilder.get()
        .setDbName(DBNAME1)
        .setTableName(TAB1)
        .setIndexName(INDEX1)
        .setIndexTableName(indexTable.getTableName())
        .setCols(Collections.singletonList(new FieldSchema("id", "int", "")))
        .setDeferredRebuild(true)
        .build(), indexTable);
  }

  @AfterClass
  public static void tearDown() throws Exception {
    DummyMetaStoreFilterHookImpl.blockResults = false;
    msc.close();
  }

  @Test
  public void testDefaultFilter() throws Exception {
    assertNotNull(msc.getTable(DBNAME1, TAB1));
    assertEquals(3, msc.getTables(DBNAME1, "*").size());
    assertEquals(3, msc.getAllTables(DBNAME1).size());
    assertEquals(1, msc.getTables(DBNAME1, TAB2).size());
    assertEquals(0, msc.getAllTables(DBNAME2).size());

    assertNotNull(msc.getDatabase(DBNAME1));
    assertEquals(3, msc.getDatabases("*").size());
    assertEquals(3, msc.getAllDatabases().size());
    assertEquals(1, msc.getDatabases(DBNAME1).size());

    assertNotNull(msc.getPartition(DBNAME1, TAB2, "name=value1"));
    assertEquals(1, msc.getPartitionsByNames(DBNAME1, TAB2, Lists.newArrayList("name=value1")).size());

    assertNotNull(msc.getIndex(DBNAME1, TAB1, INDEX1));
  }

  @Test
  public void testDummyFilterForTables() throws Exception {
    DummyMetaStoreFilterHookImpl.blockResults = true;
    try {
      msc.getTable(DBNAME1, TAB1);
      fail("getTable() should fail with blocking mode");
    } catch (NoSuchObjectException e) {
      // Excepted
    }
    assertEquals(0, msc.getTables(DBNAME1, "*").size());
    assertEquals(0, msc.getAllTables(DBNAME1).size());
    assertEquals(0, msc.getTables(DBNAME1, TAB2).size());
  }

  @Test
  public void testDummyFilterForDb() throws Exception {
    DummyMetaStoreFilterHookImpl.blockResults = true;
    try {
      assertNotNull(msc.getDatabase(DBNAME1));
      fail("getDatabase() should fail with blocking mode");
    } catch (NoSuchObjectException e) {
        // Excepted
    }
    assertEquals(0, msc.getDatabases("*").size());
    assertEquals(0, msc.getAllDatabases().size());
    assertEquals(0, msc.getDatabases(DBNAME1).size());
  }

  @Test
  public void testDummyFilterForPartition() throws Exception {
    DummyMetaStoreFilterHookImpl.blockResults = true;
    try {
      assertNotNull(msc.getPartition(DBNAME1, TAB2, "name=value1"));
      fail("getPartition() should fail with blocking mode");
    } catch (NoSuchObjectException e) {
      // Excepted
    }
    assertEquals(0, msc.getPartitionsByNames(DBNAME1, TAB2,
        Lists.newArrayList("name=value1")).size());
  }

  @Test
  public void testDummyFilterForIndex() throws Exception {
    DummyMetaStoreFilterHookImpl.blockResults = true;
    try {
      assertNotNull(msc.getIndex(DBNAME1, TAB1, INDEX1));
      fail("getPartition() should fail with blocking mode");
    } catch (NoSuchObjectException e) {
      // Excepted
    }
  }

}
