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

package org.apache.riven.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ClassUtils;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.riven.api.MetaException;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.utils.JavaUtils;

@InterfaceAudience.Private
@InterfaceStability.Evolving
public class RawStoreProxy implements InvocationHandler {

  private final RawStore base;
  private final MetaStoreInit.MetaStoreInitData metaStoreInitData =
    new MetaStoreInit.MetaStoreInitData();
  // TODO I don't understand the need for origConf vs activeConf.  Everything we read from
  // origConf seems to be read immediately upon construction.  So even if activeConf changes
  // later I don't see why we care.
  private final Configuration origConf;
  private final Configuration activeConf; // thread local conf from HMS
  private final long socketTimeout;

  protected RawStoreProxy(Configuration origConf, Configuration activeConf,
      Class<? extends RawStore> rawStoreClass, int id) throws MetaException {
    this.activeConf = activeConf;
    this.origConf = origConf;
    this.socketTimeout = MetastoreConf.getTimeVar(origConf,
        MetastoreConf.ConfVars.CLIENT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS);

    // This has to be called before initializing the instance of RawStore
    init();

    this.base = ReflectionUtils.newInstance(rawStoreClass, activeConf);
  }

  public static RawStore getProxy(Configuration origConf, Configuration activeConf, String rawStoreClassName,
      int id) throws MetaException {

    Class<? extends RawStore> baseClass = JavaUtils.getClass(rawStoreClassName, RawStore.class);

    RawStoreProxy handler = new RawStoreProxy(origConf, activeConf, baseClass, id);

    // Look for interfaces on both the class and all base classes.
    return (RawStore) Proxy.newProxyInstance(RawStoreProxy.class.getClassLoader(),
        getAllInterfaces(baseClass), handler);
  }

  private static Class<?>[] getAllInterfaces(Class<?> baseClass) {
    List interfaces = ClassUtils.getAllInterfaces(baseClass);
    Class<?>[] result = new Class<?>[interfaces.size()];
    int i = 0;
    for (Object o : interfaces) {
      result[i++] = (Class<?>)o;
    }
    return result;
  }

  private void init() throws MetaException {
    // Using the hook on startup ensures that the hook always has priority
    // over settings in *.xml.  The thread local conf needs to be used because at this point
    // it has already been initialized using hiveConf.
    MetaStoreInit.updateConnectionURL(origConf, getConf(), null, metaStoreInitData);
  }

  private void initMS() {
    base.setConf(getConf());
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      Deadline.registerIfNot(socketTimeout);
      boolean isTimerStarted = Deadline.startTimer(method.getName());
      try {
        return method.invoke(base, args);
      } finally {
        if (isTimerStarted) {
          Deadline.stopTimer();
        }
      }
    } catch (UndeclaredThrowableException e) {
      throw e.getCause();
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  public Configuration getConf() {
    return activeConf;
  }

}
