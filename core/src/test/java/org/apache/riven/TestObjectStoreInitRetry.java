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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;

import org.apache.hadoop.conf.Configuration;
import org.apache.riven.conf.MetastoreConf;
import org.apache.riven.conf.MetastoreConf.ConfVars;
import org.apache.riven.impl.ObjectStore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestObjectStoreInitRetry {

  private static boolean noisy = true; // switch to true to see line number debug traces for FakeDerby calls

  private static int injectConnectFailure = 0;

  public static void setInjectConnectFailure(int x){
    injectConnectFailure = x;
  }

  public static int getInjectConnectFailure(){
    return injectConnectFailure;
  }

  public static void decrementInjectConnectFailure(){
    injectConnectFailure--;
  }

  @BeforeClass
  public static void oneTimeSetup() throws SQLException {
    // dummy instantiation to make sure any static/ctor code blocks of that
    // driver are loaded and ready to go.
    DriverManager.registerDriver(new FakeDerby());
  }

  @AfterClass
  public static void oneTimeTearDown() throws SQLException {
    DriverManager.deregisterDriver(new FakeDerby());
  }

  public static void misbehave() throws RuntimeException{
    TestObjectStoreInitRetry.debugTrace();
    if (TestObjectStoreInitRetry.getInjectConnectFailure() > 0){
      TestObjectStoreInitRetry.decrementInjectConnectFailure();
      RuntimeException re = new JDOCanRetryException();
      if (noisy){
        System.err.println("MISBEHAVE:" + TestObjectStoreInitRetry.getInjectConnectFailure());
        re.printStackTrace(System.err);
      }
      throw re;
    }
  }

  // debug instrumenter - useful in finding which fns get called, and how often
  public static void debugTrace() {
    if (noisy){
      Exception e = new Exception();
      System.err.println("." + e.getStackTrace()[1].getLineNumber() + ":" + TestObjectStoreInitRetry.getInjectConnectFailure());
    }
  }

  protected static Configuration conf;

  @Test
  public void testObjStoreRetry() throws Exception {
    conf = MetastoreConf.newMetastoreConf();

    MetastoreConf.setLongVar(conf, ConfVars.HMSHANDLERATTEMPTS, 4);
    MetastoreConf.setTimeVar(conf, ConfVars.HMSHANDLERINTERVAL, 1, TimeUnit.SECONDS);
    MetastoreConf.setVar(conf, ConfVars.CONNECTION_DRIVER,FakeDerby.class.getName());
    MetastoreConf.setBoolVar(conf, ConfVars.TRY_DIRECT_SQL,true);
    String jdbcUrl = MetastoreConf.getVar(conf, ConfVars.CONNECTURLKEY);
    jdbcUrl = jdbcUrl.replace("derby","fderby");
    MetastoreConf.setVar(conf, ConfVars.CONNECTURLKEY,jdbcUrl);

    ObjectStore objStore = new ObjectStore();

    Exception savE = null;
    try {
      setInjectConnectFailure(5);
      objStore.setConf(conf);
    } catch (Exception e) {
      e.printStackTrace(System.err);
      savE = e;
    }

    /**
     * A note on retries.
     *
     * We've configured a total of 4 attempts.
     * 5 - 4 == 1 connect failure simulation count left after this.
     */

    assertEquals(1, getInjectConnectFailure());
    assertNotNull(savE);

    setInjectConnectFailure(0);
    objStore.setConf(conf);
    assertEquals(0, getInjectConnectFailure());
  }

  /**
   * Fake derby driver - companion class to enable testing by TestObjectStoreInitRetry
   */
  public static class FakeDerby extends org.apache.derby.jdbc.EmbeddedDriver {

    public class Connection implements java.sql.Connection {

      private java.sql.Connection _baseConn;

      public Connection(java.sql.Connection connection) {
        TestObjectStoreInitRetry.debugTrace();
        this._baseConn = connection;
      }

      @Override
      public Statement createStatement() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createStatement();
      }

      @Override
      public PreparedStatement prepareStatement(String sql) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareStatement(sql);
      }

      @Override
      public CallableStatement prepareCall(String sql) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareCall(sql);
      }

      @Override
      public String nativeSQL(String sql) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.nativeSQL(sql);
      }

      @Override
      public void setAutoCommit(boolean autoCommit) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        TestObjectStoreInitRetry.misbehave();
        _baseConn.setAutoCommit(autoCommit);
      }

      @Override
      public boolean getAutoCommit() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getAutoCommit();
      }

      @Override
      public void commit() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.commit();
      }

      @Override
      public void rollback() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.rollback();
      }

      @Override
      public void close() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.close();
      }

      @Override
      public boolean isClosed() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.isClosed();
      }

      @Override
      public DatabaseMetaData getMetaData() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getMetaData();
      }

      @Override
      public void setReadOnly(boolean readOnly) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setReadOnly(readOnly);
      }

      @Override
      public boolean isReadOnly() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.isReadOnly();
      }

      @Override
      public void setCatalog(String catalog) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setCatalog(catalog);
      }

      @Override
      public String getCatalog() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getCatalog();
      }

      @Override
      public void setTransactionIsolation(int level) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setTransactionIsolation(level);
      }

      @Override
      public int getTransactionIsolation() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getTransactionIsolation();
      }

      @Override
      public SQLWarning getWarnings() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getWarnings();
      }

      @Override
      public void clearWarnings() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.clearWarnings();
      }

      @Override
      public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createStatement(resultSetType, resultSetConcurrency);
      }

      @Override
      public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareStatement(sql, resultSetType, resultSetConcurrency);
      }

      @Override
      public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareCall(sql, resultSetType, resultSetConcurrency);
      }

      @Override
      public Map<String, Class<?>> getTypeMap() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getTypeMap();
      }

      @Override
      public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setTypeMap(map);
      }

      @Override
      public void setHoldability(int holdability) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setHoldability(holdability);
      }

      @Override
      public int getHoldability() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getHoldability();
      }

      @Override
      public Savepoint setSavepoint() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.setSavepoint();
      }

      @Override
      public Savepoint setSavepoint(String name) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.setSavepoint(name);
      }

      @Override
      public void rollback(Savepoint savepoint) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.rollback(savepoint);
      }

      @Override
      public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.releaseSavepoint(savepoint);
      }

      @Override
      public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
      }

      @Override
      public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
      }

      @Override
      public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
      }

      @Override
      public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareStatement(sql, autoGeneratedKeys);
      }

      @Override
      public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareStatement(sql, columnIndexes);
      }

      @Override
      public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.prepareStatement(sql, columnNames);
      }

      @Override
      public Clob createClob() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createClob();
      }

      @Override
      public Blob createBlob() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createBlob();
      }

      @Override
      public NClob createNClob() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createNClob();
      }

      @Override
      public SQLXML createSQLXML() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createSQLXML();
      }

      @Override
      public boolean isValid(int timeout) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.isValid(timeout);
      }

      @Override
      public void setClientInfo(String name, String value) throws SQLClientInfoException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setClientInfo(name, value);
      }

      @Override
      public void setClientInfo(Properties properties) throws SQLClientInfoException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setClientInfo(properties);
      }

      @Override
      public String getClientInfo(String name) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getClientInfo(name);
      }

      @Override
      public Properties getClientInfo() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getClientInfo();
      }

      @Override
      public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createArrayOf(typeName, elements);
      }

      @Override
      public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.createStruct(typeName, attributes);
      }

      @Override
      public void setSchema(String schema) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setSchema(schema);
      }

      @Override
      public String getSchema() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getSchema();
      }

      @Override
      public void abort(Executor executor) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.abort(executor);
      }

      @Override
      public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        _baseConn.setNetworkTimeout(executor, milliseconds);
      }

      @Override
      public int getNetworkTimeout() throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.getNetworkTimeout();
      }

      @Override
      public <T> T unwrap(Class<T> iface) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.unwrap(iface);
      }

      @Override
      public boolean isWrapperFor(Class<?> iface) throws SQLException {
        TestObjectStoreInitRetry.debugTrace();
        return _baseConn.isWrapperFor(iface);
      }
    }

    public FakeDerby(){
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
      url = url.replace("fderby","derby");
      return super.acceptsURL(url);
    }

    @Override
    public Connection connect(java.lang.String url, java.util.Properties info) throws SQLException {
      TestObjectStoreInitRetry.misbehave();
      url = url.replace("fderby","derby");
      return new FakeDerby.Connection(super.connect(url, info));
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      throw new SQLFeatureNotSupportedException(); // hope this is respected properly
    }


  }


}
