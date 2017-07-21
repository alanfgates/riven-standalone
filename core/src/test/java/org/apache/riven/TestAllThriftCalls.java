/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.riven;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.Database;
import org.apache.riven.api.PrincipalType;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.client.builder.DatabaseBuilder;
import org.apache.riven.client.builder.PrivilegeGrantInfoBuilder;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.security.HadoopThriftAuthBridge;
import org.apache.riven.utils.MetaStoreUtils;
import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * The goal of this class is to test every thrift call for MetaStoreServer.
 */
public class TestAllThriftCalls {

  private static MetaStoreClient client;

  @BeforeClass
  public static void startServer() throws Exception {
    int port = MetaStoreUtils.findFreePort();
    MetaStoreUtils.startMetaStore(port, HadoopThriftAuthBridge.getBridge());

    Configuration conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, MetastoreConf.ConfVars.THRIFT_URIS, "thrift://localhost:" + port);
    MetastoreConf.setBoolVar(conf, MetastoreConf.ConfVars.EXECUTE_SET_UGI, false);
    client = new MetaStoreClient(conf);
  }

  @Test
  public void databaseCalls() throws TException {
    String db1Name = "db1", db2Name = "db2";

    Database db1 = new DatabaseBuilder()
        .setName(db1Name)
        .setOwnerName("me")
        .setDescription("this is very descriptive")
        .setLocation("file:/tmp")
        .setOwnerType(PrincipalType.USER)
        .addParam("key", "value")
        .build();
    client.createDatabase(db1);

    Database db2 = new DatabaseBuilder()
        .setName(db2Name)
        .build();
    client.createDatabase(db2);

    Database db1retrieved = client.getDatabase(db1Name);
    Database db2retrieved = client.getDatabase(db2Name);

    Assert.assertEquals(db1, db1retrieved);
    // Other fields gets munged by the system
    Assert.assertEquals(db2.getName(), db2retrieved.getName());
  }
}
