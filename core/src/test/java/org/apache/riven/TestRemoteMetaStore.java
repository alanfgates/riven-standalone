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

import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.junit.Assert;
import org.junit.Before;


public class TestRemoteMetaStore extends TestMetastore {
  private static boolean isServerStarted = false;
  protected static int port;

  @Before
  public void startServer() throws Exception {
    isThriftClient = true;
    if (isServerStarted) {
      Assert.assertNotNull("Unable to connect to the MetaStore server", client);
      MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
      return;
    }

    port = MetaStoreUtils.findFreePort();
    System.out.println("Starting MetaStore Server on port " + port);
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge(), conf);
    isServerStarted = true;

    // This is default case with setugi off for both client and server
    client = createClient();
  }

  @Override
  protected MetaStoreClient createClient() throws Exception {
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setBoolVar(conf, ConfVars.EXECUTE_SET_UGI, false);
    return new MetaStoreClient(conf);
  }
}