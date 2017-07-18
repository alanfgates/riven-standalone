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

import org.apache.riven.api.PrincipalPrivilegeSet;
import org.apache.riven.api.PrivilegeGrantInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class PrincipalPrivilegeSetBuilder<T> extends AbstractBuilder {

  private Map<String,List<PrivilegeGrantInfo>> userPrivileges; // required
  private Map<String,List<PrivilegeGrantInfo>> groupPrivileges; // required
  private Map<String,List<PrivilegeGrantInfo>> rolePrivileges; // required
  private T child;

  public PrincipalPrivilegeSetBuilder() {
    userPrivileges = new HashMap<>();
    groupPrivileges = new HashMap<>();
    rolePrivileges = new HashMap<>();
  }

  protected void setChild(T child) {
    this.child = child;
  }

  public T setUserPrivileges(
      Map<String, List<PrivilegeGrantInfo>> userPrivileges) {
    this.userPrivileges = userPrivileges;
    return child;
  }

  public T addUserPrivilege(String user, PrivilegeGrantInfo privilege) {
    List<PrivilegeGrantInfo> pgis = userPrivileges.computeIfAbsent(user, k -> new ArrayList<>());
    pgis.add(privilege);
    return child;
  }

  public T addUserPrivilege(String user, PrivilegeGrantInfoBuilder builder) {
    return addUserPrivilege(user, builder.build());
  }

  public T setGroupPrivileges(
      Map<String, List<PrivilegeGrantInfo>> groupPrivileges) {
    this.groupPrivileges = groupPrivileges;
    return child;
  }

  public T addGroupPrivilege(String group, PrivilegeGrantInfo privilege) {
    List<PrivilegeGrantInfo> pgis = groupPrivileges.computeIfAbsent(group, k -> new ArrayList<>());
    pgis.add(privilege);
    return child;
  }

  public T addGroupPrivilege(String user, PrivilegeGrantInfoBuilder builder) {
    return addGroupPrivilege(user, builder.build());
  }

  public T setRolePrivileges(
      Map<String, List<PrivilegeGrantInfo>> rolePrivileges) {
    this.rolePrivileges = rolePrivileges;
    return child;
  }

  public T addRolePrivilege(String role, PrivilegeGrantInfo privilege) {
    List<PrivilegeGrantInfo> pgis = rolePrivileges.computeIfAbsent(role, k -> new ArrayList<>());
    pgis.add(privilege);
    return child;
  }

  public T addRolePrivilege(String user, PrivilegeGrantInfoBuilder builder) {
    return addRolePrivilege(user, builder.build());
  }

  protected PrincipalPrivilegeSet buildPPS() {
    if (userPrivileges.isEmpty() && groupPrivileges.isEmpty() && rolePrivileges.isEmpty()) {
      return null;
    }
    return new PrincipalPrivilegeSet(userPrivileges, groupPrivileges, rolePrivileges);
  }
}
