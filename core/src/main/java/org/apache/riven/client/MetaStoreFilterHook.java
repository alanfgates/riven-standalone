/**
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

package org.apache.riven.client;

import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.riven.api.Database;
import org.apache.riven.api.Index;
import org.apache.riven.api.MetaException;
import org.apache.riven.api.NoSuchObjectException;
import org.apache.riven.api.Partition;
import org.apache.riven.api.PartitionSpec;
import org.apache.riven.api.Table;

/**
 * Metadata filter hook for metastore client. This will be useful for authorization
 * plugins on hiveserver2 to filter metadata results, especially in case of
 * non-impersonation mode where the metastore doesn't know the end user's identity.
 */
@InterfaceAudience.LimitedPrivate(value = {"Apache Sentry (Incubating)" })
@InterfaceStability.Evolving
public interface MetaStoreFilterHook {

  /**
   * Filter given list of databases
   * @param dbList databases to filter
   * @return List of filtered Db names
   */
  List<String> filterDatabases(List<String> dbList) throws MetaException;

  /**
   * filter to given database object if applicable
   * @param dataBase database to filter
   * @return the same database if it's not filtered out
   * @throws NoSuchObjectException
   */
  Database filterDatabase(Database dataBase) throws MetaException, NoSuchObjectException;

  /**
   * Filter given list of tables
   * @param dbName database tables are in
   * @param tableList list of tables to filter
   * @return List of filtered table names
   */
  List<String> filterTableNames(String dbName, List<String> tableList) throws MetaException;

  /**
   * filter to given table object if applicable
   * @param table metastore table object representing the table to filter
   * @return the same table if it's not filtered out
   * @throws NoSuchObjectException
   */
  Table filterTable(Table table) throws MetaException, NoSuchObjectException;

  /**
   * Filter given list of tables
   * @param tableList list of metastore table objects to filter
   * @return List of filtered table names
   */
  List<Table> filterTables(List<Table> tableList) throws MetaException;

  /**
   * Filter given list of partitions
   * @param partitionList List of metastore partition objects to filter
   * @return List of filtered partitions
   */
  List<Partition> filterPartitions(List<Partition> partitionList) throws MetaException;

  /**
   * Filter given list of partition specs
   * @param partitionSpecList List of metastore partition spec to filter
   * @return List of filtered partition specs
   */
  List<PartitionSpec> filterPartitionSpecs(List<PartitionSpec> partitionSpecList)
      throws MetaException;

  /**
   * filter to given partition object if applicable
   * @param partition Filter a metastore partition object
   * @return the same partition object if it's not filtered out
   * @throws NoSuchObjectException
   */
  Partition filterPartition(Partition partition) throws MetaException, NoSuchObjectException;

  /**
   * Filter given list of partition names
   * @param dbName database table is in
   * @param tblName table partitions are in
   * @param partitionNames list of partition names to filter
   * @return list of filtered partition names
   */
  List<String> filterPartitionNames(String dbName, String tblName,
      List<String> partitionNames) throws MetaException;

  /**
   * Filter an index.
   * @param index metastore index object to filter
   * @return filtered object
   * @throws MetaException
   * @throws NoSuchObjectException
   */
  Index filterIndex(Index index) throws MetaException, NoSuchObjectException;

  /**
   * Filter given list of index names
   * @param dbName database table is in
   * @param tblName table indices are associated with
   * @param indexList list of index names to filter
   * @return filtered list of index names
   */
  List<String> filterIndexNames(String dbName, String tblName,
      List<String> indexList) throws MetaException;

  /**
   * Filter given list of index objects
   * @param indexeList list of index object to filter
   * @return list of filtered index objects
   */
  List<Index> filterIndexes(List<Index> indexeList) throws MetaException;
}

