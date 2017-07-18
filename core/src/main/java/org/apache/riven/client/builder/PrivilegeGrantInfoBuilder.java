/*
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
package org.apache.riven.client.builder;

import org.apache.riven.api.PrincipalType;
import org.apache.riven.api.PrivilegeGrantInfo;

public class PrivilegeGrantInfoBuilder {

  private String privilege; // required
  private int createTime; // required
  private String grantor; // required
  private PrincipalType grantorType; // required
  private boolean grantOption; // required

  public PrivilegeGrantInfoBuilder() {
    grantOption = false;
  }

  public PrivilegeGrantInfoBuilder setPrivilege(String privilege) {
    this.privilege = privilege;
    return this;
  }

  public PrivilegeGrantInfoBuilder setCreateTime(int createTime) {
    this.createTime = createTime;
    return this;
  }

  public PrivilegeGrantInfoBuilder setGrantor(String grantor) {
    this.grantor = grantor;
    return this;
  }

  public PrivilegeGrantInfoBuilder setGrantorType(PrincipalType grantorType) {
    this.grantorType = grantorType;
    return this;
  }

  public PrivilegeGrantInfoBuilder setGrantOption(boolean grantOption) {
    this.grantOption = grantOption;
    return this;
  }

  public PrivilegeGrantInfo build() {
    assert privilege != null : "privilege must be set";
    assert grantor != null : "grantor must be set";
    assert grantorType != null : "grantorType must be set";
    return new PrivilegeGrantInfo(privilege, createTime, grantor, grantorType, grantOption);
  }
}
