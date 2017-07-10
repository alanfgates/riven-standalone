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
package org.apache.riven.stats;

import org.apache.hadoop.hive.common.type.HiveDecimal;
import org.apache.riven.api.BinaryColumnStatsData;
import org.apache.riven.api.BooleanColumnStatsData;
import org.apache.riven.api.ColumnStatisticsData;
import org.apache.riven.api.ColumnStatisticsObj;
import org.apache.riven.api.Date;
import org.apache.riven.api.DateColumnStatsData;
import org.apache.riven.api.Decimal;
import org.apache.riven.api.DecimalColumnStatsData;
import org.apache.riven.api.DoubleColumnStatsData;
import org.apache.riven.api.LongColumnStatsData;
import org.apache.riven.api.StringColumnStatsData;
import org.junit.Assert;
import org.junit.Test;

public class TestStatsMerge {

  @Test
  public void binaryMerger() {

    ColumnStatisticsData data1 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats1 = new ColumnStatisticsObj("col1", "binary", data1);
    BinaryColumnStatsData stats1 = new BinaryColumnStatsData();
    stats1.setAvgColLen(15.3);
    stats1.setMaxColLen(299);
    stats1.setNumNulls(23);
    data1.setBinaryStats(stats1);

    ColumnStatisticsData data2 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats2 = new ColumnStatisticsObj("col1", "binary", data2);
    BinaryColumnStatsData stats2 = new BinaryColumnStatsData();
    stats2.setAvgColLen(10.2);
    stats2.setMaxColLen(228);
    stats2.setNumNulls(73);
    data2.setBinaryStats(stats2);

    ColumnStatsMerger merger = ColumnStatsMergerFactory.getColumnStatsMerger(colStats2, colStats1);
    merger.merge(colStats2, colStats1);

    Assert.assertEquals(15.3, colStats2.getStatsData().getBinaryStats().getAvgColLen(), 0.1);
    Assert.assertEquals(299, colStats2.getStatsData().getBinaryStats().getMaxColLen());
    Assert.assertEquals(96, colStats2.getStatsData().getBinaryStats().getNumNulls());
  }

  @Test
  public void booleanMerger() {

    ColumnStatisticsData data1 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats1 = new ColumnStatisticsObj("col1", "boolean", data1);
    BooleanColumnStatsData stats1 = new BooleanColumnStatsData();
    stats1.setNumTrues(15);
    stats1.setNumFalses(299);
    stats1.setNumNulls(23);
    data1.setBooleanStats(stats1);

    ColumnStatisticsData data2 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats2 = new ColumnStatisticsObj("col1", "boolean", data2);
    BooleanColumnStatsData stats2 = new BooleanColumnStatsData();
    stats2.setNumTrues(10);
    stats2.setNumFalses(228);
    stats2.setNumNulls(73);
    data2.setBooleanStats(stats2);

    ColumnStatsMerger merger = ColumnStatsMergerFactory.getColumnStatsMerger(colStats2, colStats1);
    merger.merge(colStats2, colStats1);

    Assert.assertEquals(25, colStats2.getStatsData().getBooleanStats().getNumTrues());
    Assert.assertEquals(527, colStats2.getStatsData().getBooleanStats().getNumFalses());
    Assert.assertEquals(96, colStats2.getStatsData().getBooleanStats().getNumNulls());
  }

  @Test
  public void dateMerger() {

    ColumnStatisticsData data1 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats1 = new ColumnStatisticsObj("col1", "date", data1);
    DateColumnStatsData stats1 = new DateColumnStatsData();
    stats1.setLowValue(new Date(896));
    stats1.setHighValue(new Date(15436));
    stats1.setNumNulls(23);
    stats1.setNumDVs(376);
    data1.setDateStats(stats1);

    ColumnStatisticsData data2 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats2 = new ColumnStatisticsObj("col1", "date", data2);
    DateColumnStatsData stats2 = new DateColumnStatsData();
    stats2.setLowValue(new Date(397));
    stats2.setHighValue(new Date(13235));
    stats2.setNumNulls(73);
    stats2.setNumDVs(876);
    data2.setDateStats(stats2);

    ColumnStatsMerger merger = ColumnStatsMergerFactory.getColumnStatsMerger(colStats2, colStats1);
    merger.merge(colStats2, colStats1);

    Assert.assertEquals(new Date(397), colStats2.getStatsData().getDateStats().getLowValue());
    Assert.assertEquals(new Date(15436), colStats2.getStatsData().getDateStats().getHighValue());
    Assert.assertEquals(96, colStats2.getStatsData().getDateStats().getNumNulls());
    Assert.assertEquals(876, colStats2.getStatsData().getDateStats().getNumDVs());
  }

  @Test
  public void decimalMerger() {

    ColumnStatisticsData data1 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats1 = new ColumnStatisticsObj("col1", "decimal", data1);
    DecimalColumnStatsData stats1 = new DecimalColumnStatsData();
    Decimal lowValue1 = new Decimal();
    lowValue1.setUnscaled(new byte[] {0x1});
    lowValue1.setScale((short)0);
    stats1.setLowValue(lowValue1);
    Decimal highValue1 = new Decimal();
    highValue1.setUnscaled(new byte[] {0x7f, 0x7f});
    highValue1.setScale((short)0);
    stats1.setHighValue(highValue1);
    stats1.setNumNulls(23);
    stats1.setNumDVs(376);
    data1.setDecimalStats(stats1);

    ColumnStatisticsData data2 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats2 = new ColumnStatisticsObj("col1", "decimal", data2);
    DecimalColumnStatsData stats2 = new DecimalColumnStatsData();
    Decimal lowValue2 = new Decimal();
    lowValue2.setUnscaled(new byte[] {0x2});
    lowValue2.setScale((short)0);
    stats2.setLowValue(lowValue2);
    Decimal highValue2 = new Decimal();
    highValue2.setUnscaled(new byte[] {0x7f, 0x7f, 0x3c});
    highValue2.setScale((short)0);
    stats2.setHighValue(highValue2);
    stats2.setNumNulls(73);
    stats2.setNumDVs(876);
    data2.setDecimalStats(stats2);

    ColumnStatsMerger merger = ColumnStatsMergerFactory.getColumnStatsMerger(colStats2, colStats1);
    merger.merge(colStats2, colStats1);

    Assert.assertEquals(lowValue1, colStats2.getStatsData().getDecimalStats().getLowValue());
    Assert.assertEquals(highValue2, colStats2.getStatsData().getDecimalStats().getHighValue());
    Assert.assertEquals(96, colStats2.getStatsData().getDecimalStats().getNumNulls());
    Assert.assertEquals(876, colStats2.getStatsData().getDecimalStats().getNumDVs());
  }

  @Test
  public void doubleMerger() {

    ColumnStatisticsData data1 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats1 = new ColumnStatisticsObj("col1", "double", data1);
    DoubleColumnStatsData stats1 = new DoubleColumnStatsData();
    stats1.setLowValue(896.3);
    stats1.setHighValue(15436.12343);
    stats1.setNumNulls(23);
    stats1.setNumDVs(376);
    data1.setDoubleStats(stats1);

    ColumnStatisticsData data2 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats2 = new ColumnStatisticsObj("col1", "double", data2);
    DoubleColumnStatsData stats2 = new DoubleColumnStatsData();
    stats2.setLowValue(39.7);
    stats2.setHighValue(132.35);
    stats2.setNumNulls(73);
    stats2.setNumDVs(876);
    data2.setDoubleStats(stats2);

    ColumnStatsMerger merger = ColumnStatsMergerFactory.getColumnStatsMerger(colStats2, colStats1);
    merger.merge(colStats2, colStats1);

    Assert.assertEquals(39.7, colStats2.getStatsData().getDoubleStats().getLowValue(), 0.1);
    Assert.assertEquals(15436.12343, colStats2.getStatsData().getDoubleStats().getHighValue(), 0.01);
    Assert.assertEquals(96, colStats2.getStatsData().getDoubleStats().getNumNulls());
    Assert.assertEquals(876, colStats2.getStatsData().getDoubleStats().getNumDVs());
  }

  @Test
  public void longMerger() {

    ColumnStatisticsData data1 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats1 = new ColumnStatisticsObj("col1", "long", data1);
    LongColumnStatsData stats1 = new LongColumnStatsData();
    stats1.setLowValue(896);
    stats1.setHighValue(15436);
    stats1.setNumNulls(23);
    stats1.setNumDVs(376);
    data1.setLongStats(stats1);

    ColumnStatisticsData data2 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats2 = new ColumnStatisticsObj("col1", "long", data2);
    LongColumnStatsData stats2 = new LongColumnStatsData();
    stats2.setLowValue(39);
    stats2.setHighValue(132);
    stats2.setNumNulls(73);
    stats2.setNumDVs(876);
    data2.setLongStats(stats2);

    ColumnStatsMerger merger = ColumnStatsMergerFactory.getColumnStatsMerger(colStats2, colStats1);
    merger.merge(colStats2, colStats1);

    Assert.assertEquals(39, colStats2.getStatsData().getLongStats().getLowValue());
    Assert.assertEquals(15436, colStats2.getStatsData().getLongStats().getHighValue());
    Assert.assertEquals(96, colStats2.getStatsData().getLongStats().getNumNulls());
    Assert.assertEquals(876, colStats2.getStatsData().getLongStats().getNumDVs());
  }

  @Test
  public void stringMerger() {

    ColumnStatisticsData data1 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats1 = new ColumnStatisticsObj("col1", "varchar", data1);
    StringColumnStatsData stats1 = new StringColumnStatsData();
    stats1.setAvgColLen(15.3);
    stats1.setMaxColLen(299);
    stats1.setNumNulls(23);
    stats1.setNumDVs(36);
    data1.setStringStats(stats1);

    ColumnStatisticsData data2 = new ColumnStatisticsData();
    ColumnStatisticsObj colStats2 = new ColumnStatisticsObj("col1", "varchar", data2);
    StringColumnStatsData stats2 = new StringColumnStatsData();
    stats2.setAvgColLen(10.2);
    stats2.setMaxColLen(228);
    stats2.setNumNulls(73);
    stats2.setNumDVs(33);
    data2.setStringStats(stats2);

    ColumnStatsMerger merger = ColumnStatsMergerFactory.getColumnStatsMerger(colStats2, colStats1);
    merger.merge(colStats2, colStats1);

    Assert.assertEquals(15.3, colStats2.getStatsData().getStringStats().getAvgColLen(), 0.1);
    Assert.assertEquals(299, colStats2.getStatsData().getStringStats().getMaxColLen());
    Assert.assertEquals(96, colStats2.getStatsData().getStringStats().getNumNulls());
    Assert.assertEquals(36, colStats2.getStatsData().getStringStats().getNumDVs());
  }

  @Test
  public void numDistinctValues() {
    // Can't really tell if this is right but at least can tell if it's reasonable.

    NumDistinctValueEstimator estimator = new NumDistinctValueEstimator(64);
    for (int i = 0; i < 100; i++) estimator.addToEstimator(i);

    Assert.assertEquals(100, estimator.estimateNumDistinctValues());

    NumDistinctValueEstimator estimator1 = new NumDistinctValueEstimator(64);
    for (int i = 50; i < 150; i++) estimator1.addToEstimator(i);

    estimator.mergeEstimators(estimator1);
    Assert.assertEquals(150.0, estimator.estimateNumDistinctValues(), 10.0);


  }
}
