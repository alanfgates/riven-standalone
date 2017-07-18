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

import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.Type;

import java.util.List;

public class TypeBuilder {

  private String name; // required
  private String type1; // optional
  private String type2; // optional
  private List<FieldSchema> fields; // optional

  public TypeBuilder() {

  }

  public TypeBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public TypeBuilder setType1(String type1) {
    this.type1 = type1;
    return this;
  }

  public TypeBuilder setType2(String type2) {
    this.type2 = type2;
    return this;
  }

  public TypeBuilder setFields(List<FieldSchema> fields) {
    this.fields = fields;
    return this;
  }

  public Type build() {
    Type t = new Type(name);
    t.setType1(type1);
    t.setType2(type2);
    t.setFields(fields);
    return t;
  }
}
