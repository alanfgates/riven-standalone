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
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.impl.MetaStoreServer;
import org.junit.Test;

/**
 * TestMetaStoreConnectionUrlHook
 * Verifies that when an instance of an implementation of RawStore is initialized, the connection
 * URL has already been updated by any metastore connect URL hooks.
 */
public class TestMetaStoreConnectionUrlHook {
  private Configuration conf;

  @Test
  public void testUrlHook() throws Exception {
    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.CONNECTURLHOOK,
        DummyJdoConnectionUrlHook.class.getName());
    MetastoreConf.setVar(conf, ConfVars.CONNECTURLKEY,
        DummyJdoConnectionUrlHook.initialUrl);
    MetastoreConf.setVar(conf, ConfVars.RAW_STORE_IMPL,
        DummyRawStoreForJdoConnection.class.getName());
    conf.setBoolean("hive.metastore.checkForDefaultDb", true);

    // Instantiating the HMSHandler with hive.metastore.checkForDefaultDb will cause it to
    // initialize an instance of the DummyRawStoreForJdoConnection
    MetaStoreServer.HMSHandler hms = new MetaStoreServer.HMSHandler(
        "test_metastore_connection_url_hook_hms_handler", conf);
  }
}
