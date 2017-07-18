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
package org.apache.riven.client.builder;

import org.apache.riven.api.Function;
import org.apache.riven.api.FunctionType;
import org.apache.riven.api.PrincipalType;
import org.apache.riven.api.ResourceType;
import org.apache.riven.api.ResourceUri;

import java.util.ArrayList;
import java.util.List;

public class FunctionBuilder {
  private String functionName; // required
  private String dbName; // required
  private String className; // required
  private String ownerName; // required
  private PrincipalType ownerType; // required
  private int createTime; // required
  private FunctionType functionType = FunctionType.JAVA; // required
  private List<ResourceUri> resourceUris; // required

  public FunctionBuilder setFunctionName(String functionName) {
    this.functionName = functionName;
    return this;
  }

  public FunctionBuilder setDbName(String dbName) {
    this.dbName = dbName;
    return this;
  }

  public FunctionBuilder setClassName(String className) {
    this.className = className;
    return this;
  }

  public FunctionBuilder setOwnerName(String ownerName) {
    this.ownerName = ownerName;
    return this;
  }

  public FunctionBuilder setOwnerType(PrincipalType ownerType) {
    this.ownerType = ownerType;
    return this;
  }

  public FunctionBuilder setCreateTime(int createTime) {
    this.createTime = createTime;
    return this;
  }

  public FunctionBuilder setFunctionType(FunctionType functionType) {
    this.functionType = functionType;
    return this;
  }

  public FunctionBuilder setResourceUris(List<ResourceUri> resourceUris) {
    this.resourceUris = resourceUris;
    return this;
  }

  public FunctionBuilder addResourceUri(ResourceType rtype, String uri) {
    if (resourceUris == null) resourceUris = new ArrayList<>();
    resourceUris.add(new ResourceUri(rtype, uri));
    return this;
  }

  public Function build() {
    assert dbName != null : "dbName must be set";
    assert functionName != null : "functionName must be set";

    return new Function(functionName, dbName, className, ownerName, ownerType, createTime,
        functionType, resourceUris);
  }
}
