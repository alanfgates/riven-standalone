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
import org.apache.riven.api.NoSuchObjectException;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.api.PrincipalType;
import org.apache.riven.api.Role;
import org.apache.riven.impl.MetaStoreServer;
import org.apache.riven.impl.RawStore;
import org.junit.Assert;
import org.junit.Test;

public class TestAdminUser {

  @Test
 public void testCreateAdminNAddUser() throws MetaException, NoSuchObjectException {
   Configuration conf = MetastoreConf.newMetastoreConf();
   MetastoreConf.setVar(conf, MetastoreConf.ConfVars.USERS_IN_ADMIN_ROLE, "adminuser");
   RawStore rawStore = new MetaStoreServer.HMSHandler("testcreateroot", conf).getMS();
   Role adminRole = rawStore.getRole(MetaStoreServer.ADMIN);
   Assert.assertTrue(adminRole.getOwnerName().equals(MetaStoreServer.ADMIN));
   Assert.assertEquals(rawStore.listPrincipalGlobalGrants(MetaStoreServer.ADMIN, PrincipalType.ROLE)
    .get(0).getGrantInfo().getPrivilege(),"All");
   Assert.assertEquals(rawStore.listRoles("adminuser", PrincipalType.USER).get(0).
     getRoleName(),MetaStoreServer.ADMIN);
 }
}