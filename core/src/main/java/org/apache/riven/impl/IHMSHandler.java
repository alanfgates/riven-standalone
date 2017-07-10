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

package org.apache.riven.impl;

import org.apache.hadoop.conf.Configurable;
import org.apache.riven.api.Database;
import org.apache.riven.api.MetaException;
import org.apache.riven.api.NoSuchObjectException;
import org.apache.riven.api.Table;
import org.apache.riven.api.ThriftHiveMetastore;
import org.apache.riven.listeners.MetaStoreEventListener;

import java.util.List;

public interface IHMSHandler extends ThriftHiveMetastore.Iface, Configurable {

  void init() throws MetaException;

  /**
   * Get the id of the thread of this handler.
   * @return thread id
   */
  int getThreadId();

  /**
   * Get a table without firing listeners and other associated machinery.  This is intended for
   * use by other code inside the server that does not need to go back through the thrift interface.
   * @param dbname database name the table is in
   * @param name name of the table
   * @return Table object
   * @throws MetaException if anything else goes wrong
   * @throws NoSuchObjectException if there is no such table
   */
  Table get_table_core(final String dbname, final String name)
      throws MetaException, NoSuchObjectException;

  /**
   * Equivalent to get_database, but does not write to audit logs, or fire pre-event listners.
   * Meant to be used for internal hive classes that don't use the thrift interface.
   * @param name database name
   * @return database object
   * @throws NoSuchObjectException if there is no such database
   * @throws MetaException if anything else goes wrong
   */
  Database get_database_core(final String name)
      throws NoSuchObjectException, MetaException;

  /**
   * Get transactional listeners for this interface.
   * @return list of transactional listeners.
   */
  List<MetaStoreEventListener> getTransactionalListeners();
}
