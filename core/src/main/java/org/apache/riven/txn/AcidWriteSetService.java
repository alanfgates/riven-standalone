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
package org.apache.riven.txn;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.RunnableConfigurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Periodically cleans WriteSet tracking information used in Transaction management
 */
public class AcidWriteSetService implements RunnableConfigurable {
  private static final Logger LOG = LoggerFactory.getLogger(AcidWriteSetService.class);

  private Configuration conf;

  @Override
  public void setConf(Configuration configuration) {
    this.conf = configuration;
  }

  @Override
  public Configuration getConf() {
    return conf;
  }

  /*
    @Override
    protected long getStartDelayMs() {
      return 0;
    }
    @Override
    protected long getIntervalMs() {
      return hiveConf.getTimeVar(HiveConf.ConfVars.WRITE_SET_REAPER_INTERVAL, TimeUnit.MILLISECONDS);
    }
    @Override
    protected Runnable getScheduedAction(HiveConf hiveConf, AtomicInteger isAliveCounter) {
      return new WriteSetReaper(hiveConf, isAliveCounter);
    }
    @Override
    public String getServiceDescription() {
      return "Periodically cleans obsolete WriteSet tracking information used in Transaction management";
    }
    private static final class WriteSetReaper implements Runnable {
      private final TxnStore txnHandler;
      private final AtomicInteger isAliveCounter;
      private WriteSetReaper(HiveConf hiveConf, AtomicInteger isAliveCounter) {
        txnHandler = TxnUtils.getTxnStore(hiveConf);
        this.isAliveCounter = isAliveCounter;
      }
      @Override
      public void run() {
        TxnStore.MutexAPI.LockHandle handle = null;
        try {
          handle = txnHandler.getMutexAPI().acquireLock(TxnStore.MUTEX_KEY.WriteSetCleaner.name());
          long startTime = System.currentTimeMillis();
          txnHandler.performWriteSetGC();
          int count = isAliveCounter.incrementAndGet();
          LOG.info("cleaner ran for " + (System.currentTimeMillis() - startTime)/1000 + "seconds.  isAliveCounter=" + count);
        }
        catch(Throwable t) {
          LOG.error("Serious error in {}", Thread.currentThread().getName(), ": {}" + t.getMessage(), t);
        }
        finally {
          if(handle != null) {
            handle.releaseLocks();
          }
        }
      }
    }
    */
  @Override
  public void run() {
    TxnStore.MutexAPI.LockHandle handle = null;
    try {
      TxnStore txnHandler = TxnUtils.getTxnStore(conf);
      handle = txnHandler.getMutexAPI().acquireLock(TxnStore.MUTEX_KEY.WriteSetCleaner.name());
      long startTime = System.currentTimeMillis();
      txnHandler.performWriteSetGC();
      LOG.debug("cleaner ran for " + (System.currentTimeMillis() - startTime)/1000 + "seconds.");
    }
    catch(Throwable t) {
      LOG.error("Serious error in {}", Thread.currentThread().getName(), ": {}" + t.getMessage(), t);
    }
    finally {
      if(handle != null) {
        handle.releaseLocks();
      }
    }
  }
}
