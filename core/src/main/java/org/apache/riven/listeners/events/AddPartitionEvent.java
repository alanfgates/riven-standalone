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

package org.apache.riven.listeners.events;

import org.apache.riven.api.Partition;
import org.apache.riven.api.Table;
import org.apache.riven.impl.IHMSHandler;
import org.apache.riven.partition.spec.PartitionSpecProxy;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AddPartitionEvent extends ListenerEvent {

  private final Table table;
  private final List<Partition> partitions;
  private PartitionSpecProxy partitionSpecProxy;

  public AddPartitionEvent(Table table, List<Partition> partitions, boolean status,
                           IHMSHandler handler) {
    super(status, handler);
    this.table = table;
    this.partitions = partitions;
    this.partitionSpecProxy = null;
  }

  public AddPartitionEvent(Table table, Partition partition, boolean status, IHMSHandler handler) {
    this(table, Arrays.asList(partition), status, handler);
  }

  /**
   * Alternative constructor to use PartitionSpec APIs.
   */
  public AddPartitionEvent(Table table, PartitionSpecProxy partitionSpec, boolean status,
                           IHMSHandler handler) {
    super(status, handler);
    this.table = table;
    this.partitions = null;
    this.partitionSpecProxy = partitionSpec;
  }

  /**
   * @return The table.
   */
  public Table getTable() {
    return table;
  }


  // Note : List<Partition> getPartitions() removed with HIVE-9609 because it will result in OOM errors with large add_partitions.

  /**
   * @return Iterator for partitions.
   */
  public Iterator<Partition> getPartitionIterator() {
    if (partitions != null){
      return partitions.iterator();
    } else {
      return partitionSpecProxy == null ? null : partitionSpecProxy.getPartitionIterator();
    }
  }

}
