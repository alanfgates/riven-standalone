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
package org.apache.riven.messaging.filters;

import org.apache.riven.api.NotificationEvent;

public class MessageFormatFilter extends BasicFilter {
  private final String format;

  public MessageFormatFilter(String format) {
    this.format = format;
  }

  @Override
  boolean shouldAccept(final NotificationEvent event) {
    if (format == null) {
      return true; // let's say that passing null in will not do any filtering.
    }
    return format.equalsIgnoreCase(event.getMessageFormat());
  }
}
