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

package org.apache.riven.conf;

import java.security.Permission;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.MetaException;
import org.apache.riven.client.MetaStoreClient;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.utils.MetaStoreUtils;
import org.apache.thrift.TException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
import org.junit.Before;

public class TestGetMetaConf {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static final Logger LOG = LoggerFactory.getLogger(TestGetMetaConf.class);
  private static Configuration conf;
  private static SecurityManager securityManager;

  private MetaStoreClient hmsc;

  public static class NoExitSecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
      // allow anything.
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
      // allow anything.
    }

    @Override
    public void checkExit(int status) {
      super.checkExit(status);
      throw new RuntimeException("System.exit() was called. Raising exception.");
    }
  }

  @AfterClass
  public static void tearDown() throws Exception {
    LOG.info("Shutting down metastore.");
    System.setSecurityManager(securityManager);
  }

  @BeforeClass
  public static void startMetaStoreServer() throws Exception {

    securityManager = System.getSecurityManager();
    System.setSecurityManager(new NoExitSecurityManager());
    Configuration metastoreConf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setBoolVar(metastoreConf, ConfVars.TRY_DIRECT_SQL_DDL, false);
    int msPort = MetaStoreUtils.startMetaStore(metastoreConf);
    conf = MetastoreConf.newMetastoreConf();
    MetastoreConf.setVar(conf, ConfVars.THRIFT_URIS, "thrift://localhost:"
        + msPort);
    MetastoreConf.setBoolVar(conf, ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    MetastoreConf.setLongVar(conf, ConfVars.THRIFT_CONNECTION_RETRIES, 10);
  }

  @Before
  public void setup() throws MetaException {
    hmsc = new MetaStoreClient(conf);
  }

  @After
  public void closeClient() {
    if (hmsc != null) {
      hmsc.close();
    }
  }

  @Test
  public void testGetMetaConfDefault() throws MetaException, TException {
    ConfVars metaConfVar = ConfVars.TRY_DIRECT_SQL;
    String expected = metaConfVar.defaultVal.toString();
    String actual = hmsc.getMetaConf(metaConfVar.toString());
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMetaConfDefaultEmptyString() throws MetaException, TException {
    ConfVars metaConfVar = ConfVars.PARTITION_NAME_WHITELIST_PATTERN;
    String expected = "";
    String actual = hmsc.getMetaConf(metaConfVar.toString());
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMetaConfOverridden() throws MetaException, TException {
    ConfVars metaConfVar = ConfVars.TRY_DIRECT_SQL_DDL;
    String expected = "false";
    String actual = hmsc.getMetaConf(metaConfVar.toString());
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMetaConfUnknownPreperty() throws MetaException, TException {
    String unknownPropertyName = "hive.meta.foo.bar";
    thrown.expect(MetaException.class);
    thrown.expectMessage("Invalid configuration key " + unknownPropertyName);
    hmsc.getMetaConf(unknownPropertyName);
  }
}
