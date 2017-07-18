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

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.Database;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.Index;
import org.apache.riven.api.Partition;
import org.apache.riven.api.PartitionEventType;
import org.apache.riven.api.Table;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.client.builder.DatabaseBuilder;
import org.apache.riven.client.builder.IndexBuilder;
import org.apache.riven.client.builder.PartitionBuilder;
import org.apache.riven.client.builder.TableBuilder;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.listeners.MetaStoreEventListener;
import org.apache.riven.listeners.MetaStorePreEventListener;
import org.apache.riven.listeners.events.AddIndexEvent;
import org.apache.riven.listeners.events.AddPartitionEvent;
import org.apache.riven.listeners.events.AlterIndexEvent;
import org.apache.riven.listeners.events.AlterPartitionEvent;
import org.apache.riven.listeners.events.AlterTableEvent;
import org.apache.riven.listeners.events.ConfigChangeEvent;
import org.apache.riven.listeners.events.CreateDatabaseEvent;
import org.apache.riven.listeners.events.CreateTableEvent;
import org.apache.riven.listeners.events.DropDatabaseEvent;
import org.apache.riven.listeners.events.DropIndexEvent;
import org.apache.riven.listeners.events.DropPartitionEvent;
import org.apache.riven.listeners.events.DropTableEvent;
import org.apache.riven.listeners.events.ListenerEvent;
import org.apache.riven.listeners.events.LoadPartitionDoneEvent;
import org.apache.riven.listeners.events.PreAddIndexEvent;
import org.apache.riven.listeners.events.PreAddPartitionEvent;
import org.apache.riven.listeners.events.PreAlterIndexEvent;
import org.apache.riven.listeners.events.PreAlterPartitionEvent;
import org.apache.riven.listeners.events.PreAlterTableEvent;
import org.apache.riven.listeners.events.PreCreateDatabaseEvent;
import org.apache.riven.listeners.events.PreCreateTableEvent;
import org.apache.riven.listeners.events.PreDropDatabaseEvent;
import org.apache.riven.listeners.events.PreDropIndexEvent;
import org.apache.riven.listeners.events.PreDropPartitionEvent;
import org.apache.riven.listeners.events.PreDropTableEvent;
import org.apache.riven.listeners.events.PreEventContext;
import org.apache.riven.listeners.events.PreLoadPartitionDoneEvent;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TestMetaStoreEventListener. Test case for
 * {@link MetaStoreEventListener} and
 * {@link MetaStorePreEventListener}
 */
public class TestMetaStoreEventListener {
  private Configuration conf;
  private MetaStoreClient msc;

  private static final String dbName = "hive2038";
  private static final String tblName = "tmptbl";
  private static final String renamed = "tmptbl2";

  @Before
  public void setUp() throws Exception {

    System.setProperty("metastore.event.listeners",
        DummyListener.class.getName());
    System.setProperty("metastore.pre.event.listeners",
        DummyPreListener.class.getName());

    int port = org.apache.riven.utils.MetaStoreUtils.findFreePort();
    org.apache.riven.utils.MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge());

    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 3);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    msc = new MetaStoreClient(conf);

    DummyListener.notifyList.clear();
    DummyPreListener.notifyList.clear();
  }


  private void validateCreateDb(Database expectedDb, Database actualDb) {
    Assert.assertEquals(expectedDb.getName(), actualDb.getName());
    Assert.assertEquals(expectedDb.getLocationUri(), actualDb.getLocationUri());
  }

  private void validateTable(Table expectedTable, Table actualTable) {
    Assert.assertEquals(expectedTable.getTableName(), actualTable.getTableName());
    Assert.assertEquals(expectedTable.getDbName(), actualTable.getDbName());
    Assert.assertEquals(expectedTable.getSd().getLocation(), actualTable.getSd().getLocation());
  }

  private void validateCreateTable(Table expectedTable, Table actualTable) {
    validateTable(expectedTable, actualTable);
  }

  private void validateAddPartition(Partition expectedPartition, Partition actualPartition) {
    Assert.assertEquals(expectedPartition, actualPartition);
  }

  private void validateTableInAddPartition(Table expectedTable, Table actualTable) {
    Assert.assertEquals(expectedTable, actualTable);
  }

  private void validatePartition(Partition expectedPartition, Partition actualPartition) {
    Assert.assertEquals(expectedPartition.getValues(), actualPartition.getValues());
    Assert.assertEquals(expectedPartition.getDbName(), actualPartition.getDbName());
    Assert.assertEquals(expectedPartition.getTableName(), actualPartition.getTableName());
  }

  private void validateAlterPartition(Partition expectedOldPartition,
      Partition expectedNewPartition, String actualOldPartitionDbName,
      String actualOldPartitionTblName,List<String> actualOldPartitionValues,
      Partition actualNewPartition) {
    Assert.assertEquals(expectedOldPartition.getValues(), actualOldPartitionValues);
    Assert.assertEquals(expectedOldPartition.getDbName(), actualOldPartitionDbName);
    Assert.assertEquals(expectedOldPartition.getTableName(), actualOldPartitionTblName);

    validatePartition(expectedNewPartition, actualNewPartition);
  }

  private void validateAlterTable(Table expectedOldTable, Table expectedNewTable,
      Table actualOldTable, Table actualNewTable) {
    validateTable(expectedOldTable, actualOldTable);
    validateTable(expectedNewTable, actualNewTable);
  }

  private void validateAlterTableColumns(Table expectedOldTable, Table expectedNewTable,
      Table actualOldTable, Table actualNewTable) {
    validateAlterTable(expectedOldTable, expectedNewTable, actualOldTable, actualNewTable);

    Assert.assertEquals(expectedOldTable.getSd().getCols(), actualOldTable.getSd().getCols());
    Assert.assertEquals(expectedNewTable.getSd().getCols(), actualNewTable.getSd().getCols());
  }

  private void validateLoadPartitionDone(String expectedTableName,
      Map<String,String> expectedPartitionName, String actualTableName,
      Map<String,String> actualPartitionName) {
    Assert.assertEquals(expectedPartitionName, actualPartitionName);
    Assert.assertEquals(expectedTableName, actualTableName);
  }

  private void validateDropPartition(Iterator<Partition> expectedPartitions, Iterator<Partition> actualPartitions) {
    while (expectedPartitions.hasNext()){
      Assert.assertTrue(actualPartitions.hasNext());
      validatePartition(expectedPartitions.next(), actualPartitions.next());
    }
    Assert.assertFalse(actualPartitions.hasNext());
  }

  private void validateTableInDropPartition(Table expectedTable, Table actualTable) {
    validateTable(expectedTable, actualTable);
  }

  private void validateDropTable(Table expectedTable, Table actualTable) {
    validateTable(expectedTable, actualTable);
  }

  private void validateDropDb(Database expectedDb, Database actualDb) {
    Assert.assertEquals(expectedDb, actualDb);
  }

  private void validateIndex(Index expectedIndex, Index actualIndex) {
    Assert.assertEquals(expectedIndex.getDbName(), actualIndex.getDbName());
    Assert.assertEquals(expectedIndex.getIndexName(), actualIndex.getIndexName());
    Assert.assertEquals(expectedIndex.getIndexHandlerClass(), actualIndex.getIndexHandlerClass());
    Assert.assertEquals(expectedIndex.getOrigTableName(), actualIndex.getOrigTableName());
    Assert.assertEquals(expectedIndex.getIndexTableName(), actualIndex.getIndexTableName());
    Assert.assertEquals(expectedIndex.getSd().getLocation(), actualIndex.getSd().getLocation());
  }

  private void validateAddIndex(Index expectedIndex, Index actualIndex) {
    validateIndex(expectedIndex, actualIndex);
  }

  private void validateAlterIndex(Index expectedOldIndex, Index actualOldIndex,
      Index expectedNewIndex, Index actualNewIndex) {
    validateIndex(expectedOldIndex, actualOldIndex);
    validateIndex(expectedNewIndex, actualNewIndex);
  }

  private void validateDropIndex(Index expectedIndex, Index actualIndex) {
    validateIndex(expectedIndex, actualIndex);
  }

  @Test
  public void testListener() throws Exception {
    int listSize = 0;

    List<ListenerEvent> notifyList = DummyListener.notifyList;
    List<PreEventContext> preNotifyList = DummyPreListener.notifyList;
    Assert.assertEquals(notifyList.size(), listSize);
    Assert.assertEquals(preNotifyList.size(), listSize);

    msc.createDatabase(new DatabaseBuilder()
        .setName(dbName)
        .build()
    );
    listSize++;
    PreCreateDatabaseEvent preDbEvent = (PreCreateDatabaseEvent)(preNotifyList.get(preNotifyList.size() - 1));
    Database db = msc.getDatabase(dbName);
    Assert.assertEquals(listSize, notifyList.size());
    Assert.assertEquals(listSize + 1, preNotifyList.size());
    validateCreateDb(db, preDbEvent.getDatabase());

    CreateDatabaseEvent dbEvent = (CreateDatabaseEvent)(notifyList.get(listSize - 1));
    assert dbEvent.getStatus();
    validateCreateDb(db, dbEvent.getDatabase());

    msc.createTable(new TableBuilder()
        .setDbName(dbName)
        .setTableName(tblName)
        .setCols(Collections.singletonList(new FieldSchema("a", "string", "")))
        .setPartCols(Collections.singletonList(new FieldSchema("b", "string", "")))
        .build()
    );
    PreCreateTableEvent preTblEvent = (PreCreateTableEvent)(preNotifyList.get(preNotifyList.size() - 1));
    listSize++;
    Table tbl = msc.getTable(dbName, tblName);
    validateCreateTable(tbl, preTblEvent.getTable());
    Assert.assertEquals(notifyList.size(), listSize);

    CreateTableEvent tblEvent = (CreateTableEvent)(notifyList.get(listSize - 1));
    assert tblEvent.getStatus();
    validateCreateTable(tbl, tblEvent.getTable());

    String indexName = "tmptbl_i";
    Table indexTable = new TableBuilder()
        .setDbName(dbName)
        .setTableName(indexName + tblName)
        .setCols(Collections.singletonList(new FieldSchema("a", "string", "")))
        .build();
    Map<String, String> indexProperties = new HashMap<>();
    indexProperties.put("prop1", "val1");
    indexProperties.put("prop2", "val2");
    Index index = new IndexBuilder()
        .setIndexName(indexName)
        .setDbName(dbName)
        .setTableName(tblName)
        .setIndexTableName(indexTable.getTableName())
        .setCols(Collections.singletonList(new FieldSchema("a", "string", "")))
        .setDeferredRebuild(true)
        .setIndexParams(indexProperties)
        .build();
    msc.createIndex(index, indexTable);
    listSize += 2;  // creates index table internally
    Assert.assertEquals(notifyList.size(), listSize);

    AddIndexEvent addIndexEvent = (AddIndexEvent)notifyList.get(listSize - 1);
    assert addIndexEvent.getStatus();
    PreAddIndexEvent preAddIndexEvent = (PreAddIndexEvent)(preNotifyList.get(preNotifyList.size() - 2));

    Index oldIndex = msc.getIndex(dbName, "tmptbl", indexName);

    validateAddIndex(oldIndex, addIndexEvent.getIndex());

    validateAddIndex(oldIndex, preAddIndexEvent.getIndex());

    indexProperties.put("prop1", "val1_new");
    indexProperties.put("prop3", "val3");
    msc.alter_index(dbName, tblName, indexName, index);

    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);

    Index newIndex = msc.getIndex(dbName, tblName, indexName);

    AlterIndexEvent alterIndexEvent = (AlterIndexEvent) notifyList.get(listSize - 1);
    assert alterIndexEvent.getStatus();
    validateAlterIndex(oldIndex, alterIndexEvent.getOldIndex(),
        newIndex, alterIndexEvent.getNewIndex());

    PreAlterIndexEvent preAlterIndexEvent = (PreAlterIndexEvent) (preNotifyList.get(preNotifyList.size() - 1));
    validateAlterIndex(oldIndex, preAlterIndexEvent.getOldIndex(),
        newIndex, preAlterIndexEvent.getNewIndex());

    msc.dropIndex(dbName, tblName, indexName, true);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);

    DropIndexEvent dropIndexEvent = (DropIndexEvent) notifyList.get(listSize - 1);
    assert dropIndexEvent.getStatus();
    validateDropIndex(newIndex, dropIndexEvent.getIndex());

    PreDropIndexEvent preDropIndexEvent = (PreDropIndexEvent) (preNotifyList.get(preNotifyList.size() - 1));
    validateDropIndex(newIndex, preDropIndexEvent.getIndex());

    msc.add_partition(new PartitionBuilder()
        .setDbName(dbName)
        .setTableName(tblName)
        .setValues(Collections.singletonList("2011"))
        .setCols(Collections.singletonList(new FieldSchema("a", "string", "")))
        .build()
    );
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    PreAddPartitionEvent prePartEvent = (PreAddPartitionEvent)(preNotifyList.get(preNotifyList.size() - 1));

    AddPartitionEvent partEvent = (AddPartitionEvent)(notifyList.get(listSize-1));
    assert partEvent.getStatus();
    Partition part = msc.getPartition("hive2038", "tmptbl", "b=2011");
    Partition partAdded = partEvent.getPartitionIterator().next();
    validateAddPartition(part, partAdded);
    validateTableInAddPartition(tbl, partEvent.getTable());
    validateAddPartition(part, prePartEvent.getPartitions().get(0));

    // Test adding multiple partitions in a single partition-set, atomically.
    int currentTime = (int)System.currentTimeMillis();
    MetaStoreClient hmsClient = new MetaStoreClient(conf);
    Table table = hmsClient.getTable(dbName, "tmptbl");
    Partition partition1 = new Partition(Arrays.asList("20110101"), dbName, "tmptbl", currentTime,
                                        currentTime, table.getSd(), table.getParameters());
    Partition partition2 = new Partition(Arrays.asList("20110102"), dbName, "tmptbl", currentTime,
                                        currentTime, table.getSd(), table.getParameters());
    Partition partition3 = new Partition(Arrays.asList("20110103"), dbName, "tmptbl", currentTime,
                                        currentTime, table.getSd(), table.getParameters());
    hmsClient.add_partitions(Arrays.asList(partition1, partition2, partition3));
    ++listSize;
    AddPartitionEvent multiplePartitionEvent = (AddPartitionEvent)(notifyList.get(listSize-1));
    Assert.assertEquals("Unexpected table value.", table, multiplePartitionEvent.getTable());
    List<Partition> multiParts = Lists.newArrayList(multiplePartitionEvent.getPartitionIterator());
    Assert.assertEquals("Unexpected number of partitions in event!", 3, multiParts.size());
    Assert.assertEquals("Unexpected partition value.", partition1.getValues(), multiParts.get(0).getValues());
    Assert.assertEquals("Unexpected partition value.", partition2.getValues(), multiParts.get(1).getValues());
    Assert.assertEquals("Unexpected partition value.", partition3.getValues(), multiParts.get(2).getValues());

    part.setLastAccessTime((int)(System.currentTimeMillis() % 1000));
    msc.alter_partition(dbName, tblName, part, null);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    PreAlterPartitionEvent preAlterPartEvent =
        (PreAlterPartitionEvent)preNotifyList.get(preNotifyList.size() - 1);

    //the partition did not change,
    // so the new partition should be similar to the original partition
    Partition origP = msc.getPartition(dbName, tblName, "b=2011");

    AlterPartitionEvent alterPartEvent = (AlterPartitionEvent)notifyList.get(listSize - 1);
    assert alterPartEvent.getStatus();
    validateAlterPartition(origP, origP, alterPartEvent.getOldPartition().getDbName(),
        alterPartEvent.getOldPartition().getTableName(),
        alterPartEvent.getOldPartition().getValues(), alterPartEvent.getNewPartition());


    validateAlterPartition(origP, origP, preAlterPartEvent.getDbName(),
        preAlterPartEvent.getTableName(), preAlterPartEvent.getNewPartition().getValues(),
        preAlterPartEvent.getNewPartition());

    List<String> part_vals = new ArrayList<String>();
    part_vals.add("c=2012");
    int preEventListSize;
    preEventListSize = preNotifyList.size() + 1;
    Partition newPart = msc.appendPartition(dbName, tblName, part_vals);

    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    Assert.assertEquals(preNotifyList.size(), preEventListSize);

    AddPartitionEvent appendPartEvent =
        (AddPartitionEvent)(notifyList.get(listSize-1));
    Partition partAppended = appendPartEvent.getPartitionIterator().next();
    validateAddPartition(newPart, partAppended);

    PreAddPartitionEvent preAppendPartEvent =
        (PreAddPartitionEvent)(preNotifyList.get(preNotifyList.size() - 1));
    validateAddPartition(newPart, preAppendPartEvent.getPartitions().get(0));

    Table t = msc.getTable(dbName, tblName);
    t.setTableName(renamed);
    msc.alter_table(dbName, tblName, t);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    PreAlterTableEvent preAlterTableE = (PreAlterTableEvent) preNotifyList.get(preNotifyList.size() - 1);

    Table renamedTable = msc.getTable(dbName, renamed);

    AlterTableEvent alterTableE = (AlterTableEvent) notifyList.get(listSize-1);
    assert alterTableE.getStatus();
    validateAlterTable(tbl, renamedTable, alterTableE.getOldTable(), alterTableE.getNewTable());
    validateAlterTable(tbl, renamedTable, preAlterTableE.getOldTable(),
        preAlterTableE.getNewTable());

    //change the table name back
    t = msc.getTable(dbName, renamed);
    t.setTableName(tblName);
    msc.alter_table(dbName, renamed, t);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);

    t = msc.getTable(dbName, tblName);
    t.getSd().getCols().add(new FieldSchema("c", "int", ""));
    msc.alter_table(dbName, tblName, t);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    preAlterTableE = (PreAlterTableEvent) preNotifyList.get(preNotifyList.size() - 1);

    Table altTable = msc.getTable(dbName, tblName);

    alterTableE = (AlterTableEvent) notifyList.get(listSize-1);
    assert alterTableE.getStatus();
    validateAlterTableColumns(tbl, altTable, alterTableE.getOldTable(), alterTableE.getNewTable());
    validateAlterTableColumns(tbl, altTable, preAlterTableE.getOldTable(),
        preAlterTableE.getNewTable());

    Map<String,String> kvs = new HashMap<String, String>(1);
    kvs.put("b", "2011");
    msc.markPartitionForEvent("hive2038", "tmptbl", kvs, PartitionEventType.LOAD_DONE);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);

    LoadPartitionDoneEvent partMarkEvent = (LoadPartitionDoneEvent)notifyList.get(listSize - 1);
    assert partMarkEvent.getStatus();
    validateLoadPartitionDone("tmptbl", kvs, partMarkEvent.getTable().getTableName(),
        partMarkEvent.getPartitionName());

    PreLoadPartitionDoneEvent prePartMarkEvent =
        (PreLoadPartitionDoneEvent)preNotifyList.get(preNotifyList.size() - 1);
    validateLoadPartitionDone("tmptbl", kvs, prePartMarkEvent.getTableName(),
        prePartMarkEvent.getPartitionName());

    msc.dropPartition(dbName, tblName, Collections.singletonList("2011"));
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    PreDropPartitionEvent preDropPart = (PreDropPartitionEvent) preNotifyList.get(preNotifyList
        .size() - 1);

    DropPartitionEvent dropPart = (DropPartitionEvent)notifyList.get(listSize - 1);
    assert dropPart.getStatus();
    validateDropPartition(Collections.singletonList(part).iterator(), dropPart.getPartitionIterator());
    validateTableInDropPartition(tbl, dropPart.getTable());

    validateDropPartition(Collections.singletonList(part).iterator(), preDropPart.getPartitionIterator());
    validateTableInDropPartition(tbl, preDropPart.getTable());

    msc.dropTable(dbName, tblName);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    PreDropTableEvent preDropTbl = (PreDropTableEvent)preNotifyList.get(preNotifyList.size() - 1);

    DropTableEvent dropTbl = (DropTableEvent)notifyList.get(listSize-1);
    assert dropTbl.getStatus();
    validateDropTable(tbl, dropTbl.getTable());
    validateDropTable(tbl, preDropTbl.getTable());

    msc.dropDatabase(dbName, true, false, false);
    listSize++;
    Assert.assertEquals(notifyList.size(), listSize);
    PreDropDatabaseEvent preDropDB = (PreDropDatabaseEvent)preNotifyList.get(preNotifyList.size() - 1);

    DropDatabaseEvent dropDB = (DropDatabaseEvent)notifyList.get(listSize-1);
    assert dropDB.getStatus();
    validateDropDb(db, dropDB.getDatabase());
    validateDropDb(db, preDropDB.getDatabase());

    msc.setMetaConf("metastore.try.direct.sql", "false");
    ConfigChangeEvent event = (ConfigChangeEvent) notifyList.get(notifyList.size() - 1);
    Assert.assertEquals("metastore.try.direct.sql", event.getKey());
    Assert.assertEquals("true", event.getOldValue());
    Assert.assertEquals("false", event.getNewValue());
  }
}
