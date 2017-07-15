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
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.MetaException;
import org.apache.riven.listeners.MetaStoreEventListener;
import org.apache.riven.listeners.events.AddIndexEvent;
import org.apache.riven.listeners.events.AddPartitionEvent;
import org.apache.riven.listeners.events.AlterIndexEvent;
import org.apache.riven.listeners.events.AlterPartitionEvent;
import org.apache.riven.listeners.events.AlterTableEvent;
import org.apache.riven.listeners.events.ConfigChangeEvent;
import org.apache.riven.listeners.events.CreateDatabaseEvent;
import org.apache.riven.listeners.events.CreateFunctionEvent;
import org.apache.riven.listeners.events.CreateTableEvent;
import org.apache.riven.listeners.events.DropDatabaseEvent;
import org.apache.riven.listeners.events.DropFunctionEvent;
import org.apache.riven.listeners.events.DropIndexEvent;
import org.apache.riven.listeners.events.DropPartitionEvent;
import org.apache.riven.listeners.events.DropTableEvent;
import org.apache.riven.listeners.events.ListenerEvent;
import org.apache.riven.listeners.events.LoadPartitionDoneEvent;

/** A dummy implementation for
 * {@link MetaStoreEventListener}
 * for testing purposes.
 */
public class DummyListener extends MetaStoreEventListener{

  static final List<ListenerEvent> notifyList = new ArrayList<>();

  /**
   * @return The last event received, or null if no event was received.
   */
  public static ListenerEvent getLastEvent() {
    if (notifyList.isEmpty()) {
      return null;
    } else {
      return notifyList.get(notifyList.size() - 1);
    }
  }

  public DummyListener(Configuration config) {
    super(config);
  }

  @Override
  public void onConfigChange(ConfigChangeEvent configChange) {
    addEvent(configChange);
  }

  @Override
  public void onAddPartition(AddPartitionEvent partition) throws MetaException {
    addEvent(partition);
  }

  @Override
  public void onCreateDatabase(CreateDatabaseEvent db) throws MetaException {
    addEvent(db);
  }

  @Override
  public void onCreateTable(CreateTableEvent table) throws MetaException {
    addEvent(table);
  }

  @Override
  public void onDropDatabase(DropDatabaseEvent db) throws MetaException {
    addEvent(db);
  }

  @Override
  public void onDropPartition(DropPartitionEvent partition) throws MetaException {
    addEvent(partition);
  }

  @Override
  public void onDropTable(DropTableEvent table) throws MetaException {
    addEvent(table);
  }

  @Override
  public void onAlterTable(AlterTableEvent event) throws MetaException {
    addEvent(event);
  }

  @Override
  public void onAlterPartition(AlterPartitionEvent event) throws MetaException {
    addEvent(event);
  }

  @Override
  public void onLoadPartitionDone(LoadPartitionDoneEvent partEvent) throws MetaException {
    addEvent(partEvent);
  }

  @Override
  public void onAddIndex(AddIndexEvent indexEvent) throws MetaException {
    addEvent(indexEvent);
  }

  @Override
  public void onDropIndex(DropIndexEvent indexEvent) throws MetaException {
    addEvent(indexEvent);
  }

  @Override
  public void onAlterIndex(AlterIndexEvent indexEvent) throws MetaException {
    addEvent(indexEvent);
  }

  @Override
  public void onCreateFunction (CreateFunctionEvent fnEvent) throws MetaException {
    addEvent(fnEvent);
  }

  @Override
  public void onDropFunction (DropFunctionEvent fnEvent) throws MetaException {
    addEvent(fnEvent);
  }

  private void addEvent(ListenerEvent event) {
    notifyList.add(event);
  }
}
