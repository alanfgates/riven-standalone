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

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.impl.MetaStoreServer.HMSHandler;
import org.apache.riven.api.MetaException;
import org.apache.riven.listeners.MetaStoreEventListener;
import org.apache.riven.listeners.events.AddPartitionEvent;
import org.apache.riven.listeners.events.AlterPartitionEvent;
import org.apache.riven.listeners.events.AlterTableEvent;
import org.apache.riven.listeners.events.CreateDatabaseEvent;
import org.apache.riven.listeners.events.CreateTableEvent;
import org.apache.riven.listeners.events.DropDatabaseEvent;
import org.apache.riven.listeners.events.DropPartitionEvent;
import org.apache.riven.listeners.events.DropTableEvent;
import org.apache.riven.listeners.events.LoadPartitionDoneEvent;
import org.junit.Assert;

/** An implementation for MetaStoreEventListener which checks that the IP Address stored in
 * HMSHandler matches that of local host, for testing purposes.
 */
public class IpAddressListener extends MetaStoreEventListener {

  private static final String LOCAL_HOST = "localhost";

  public IpAddressListener(Configuration config) {
    super(config);
  }

  private void checkIpAddress() {
    try {
      String localhostIp = InetAddress.getByName(LOCAL_HOST).getHostAddress();
      Assert.assertEquals(localhostIp, HMSHandler.getThreadLocalIpAddress());
    } catch (UnknownHostException e) {
      Assert.assertTrue("InetAddress.getLocalHost threw an exception: " + e.getMessage(), false);
    }
  }

  @Override
  public void onAddPartition(AddPartitionEvent partition) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onCreateDatabase(CreateDatabaseEvent db) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onCreateTable(CreateTableEvent table) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onDropDatabase(DropDatabaseEvent db) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onDropPartition(DropPartitionEvent partition) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onDropTable(DropTableEvent table) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onAlterTable(AlterTableEvent event) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onAlterPartition(AlterPartitionEvent event) throws MetaException {
    checkIpAddress();
  }

  @Override
  public void onLoadPartitionDone(LoadPartitionDoneEvent partEvent) throws MetaException {
    checkIpAddress();
  }
}
