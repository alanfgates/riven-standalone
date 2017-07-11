/**
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

package org.apache.riven.client;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.riven.api.MetaException;
import org.apache.riven.api.Table;

/**
 * HiveMetaHookLoader is responsible for loading a {@link MetaHook}
 * for a given table.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public interface MetaHookLoader {
  /**
   * Loads a hook for the specified table.
   *
   * @param tbl table of interest
   *
   * @return hook, or null if none registered
   */
  MetaHook getHook(Table tbl) throws MetaException;
}

// End HiveMetaHookLoader.java
