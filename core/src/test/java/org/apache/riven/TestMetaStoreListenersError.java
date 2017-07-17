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
import org.apache.riven.api.MetaException;
import org.apache.riven.impl.MetaStoreServer;
import org.apache.riven.listeners.MetaStoreEventListener;
import org.apache.riven.listeners.MetaStoreInitContext;
import org.apache.riven.listeners.MetaStoreInitListener;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for unwrapping InvocationTargetException, which is thrown from
 * constructor of listener class
 */
public class TestMetaStoreListenersError {

  @Test
  public void testInitListenerException() throws Throwable {

    System.setProperty("metastore.init.hooks", ErrorInitListener.class.getName());
    int port = MetaStoreUtils.findFreePort();
    try {
      MetaStoreServer.startMetaStore(port, HadoopThriftAuthBridge.getBridge());
    } catch (Throwable throwable) {
      Assert.assertEquals(MetaException.class, throwable.getClass());
      Assert.assertEquals(
          "Failed to instantiate listener named: " +
              "org.apache.riven.TestMetaStoreListenersError$ErrorInitListener, " +
              "reason: java.lang.IllegalArgumentException: exception on constructor",
          throwable.getMessage());
    }
  }

  @Test
  public void testEventListenerException() throws Throwable {

    System.setProperty("metastore.init.hooks", "");
    System.setProperty("metastore.event.listeners", ErrorEventListener.class.getName());
    int port = MetaStoreUtils.findFreePort();
    try {
      MetaStoreServer.startMetaStore(port, HadoopThriftAuthBridge.getBridge());
    } catch (Throwable throwable) {
      Assert.assertEquals(MetaException.class, throwable.getClass());
      Assert.assertEquals(
          "Failed to instantiate listener named: " +
              "org.apache.riven.TestMetaStoreListenersError$ErrorEventListener, " +
              "reason: java.lang.IllegalArgumentException: exception on constructor",
          throwable.getMessage());
    }
  }

  public static class ErrorInitListener extends MetaStoreInitListener {

    public ErrorInitListener(Configuration config) {
      super(config);
      throw new IllegalArgumentException("exception on constructor");
    }

    public void onInit(MetaStoreInitContext context) throws MetaException {
    }
  }

  public static class ErrorEventListener extends MetaStoreEventListener {

    public ErrorEventListener(Configuration config) {
      super(config);
      throw new IllegalArgumentException("exception on constructor");
    }
  }
}
