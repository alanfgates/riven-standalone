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
package org.apache.riven.impl;

import org.apache.riven.PartitionExpressionProxy;
import org.apache.riven.api.FieldSchema;
import org.apache.riven.api.MetaException;

import java.util.List;

/**
 * This default implementation throws whenever called.  The metastore doesn't currently have the
 * logic to parse expressions.  This implementation has to be provided from the outside.
 */
public class DefaultPartitionExpressionProxy implements PartitionExpressionProxy {
  @Override
  public String convertExprToFilter(byte[] expr) throws MetaException {
    throw new UnsupportedOperationException("Partition expression parsing not supported.");
  }

  @Override
  public boolean filterPartitionsByExpr(List<FieldSchema> partColumns, byte[] expr,
                                        String defaultPartitionName,
                                        List<String> partitionNames) throws MetaException {
    throw new UnsupportedOperationException("Partition expression parsing not supported.");
  }
}
