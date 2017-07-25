#!/usr/bin/env bash

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# This file tests the metastore start/stop script.
# The tests are not run automatically as part of the unit tests as they take a while, but should
# be run manually anytime the metastore script is changed.

function fail {
  echo $1
  exit $2
}

# arg1 = message to print if it fails
function find_pid {
  MSG=$1
  [ -f $PID_FILE ] || fail "Cannot find pid file $PID_FILE :$MSG" 2
  PID=`cat $PID_FILE`
  [ `ps -p $PID | grep $PID | wc -l` == "1" ] || fail "Cannot find pid $PID : $MSG" 2
}

function confirm_no_pid {
  MSG=$1
  [ -f $PID_FILE ] || fail "Cannot find pid file $PID_FILE :$MSG" 2
  PID=`cat $PID_FILE`
  [ `ps -p $PID | grep $PID | wc -l` == "0" ] || fail "pid $PID still exists : $MSG" 2
}

# arg1 = TMPDIR
# arg2 = BINARY_DIST
function test_simple_deployment {
  TMPDIR=$1
  BINARY_DIST=$2
  DIR=$TMPDIR/simple_deployment
  mkdir $DIR
  cp $BINARY_DIST $DIR
  cd $DIR
  TAR=`basename $BINARY_DIST`
  echo "Going to run tar zxf $TAR"
  tar zxf $TAR || fail "Unable to untar distribution" 2
  DIST_DIR=`echo $TAR | sed s/\.tar.\gz//`
  cd $DIST_DIR || fail "Couldn't cd to $DIST_DIR" 2
  [ -f bin/metastore ] || fail "Cannot find metastore script" 2

  # Test help
  echo "Testing --help option"
  HELP_OUTPUT=$TMPDIR/help_output
  bin/metastore --help > $HELP_OUTPUT
  grep "Usage:" $HELP_OUTPUT || fail "Asking for help didn't get us any" 2

  # Test bogus option gets help
  echo "Testing bogus option"
  HELP_OUTPUT=$TMPDIR/help_output
  bin/metastore --nosuchoption > $HELP_OUTPUT
  grep "Usage:" $HELP_OUTPUT || fail "Asking for bogus option didn't get us help" 2

  # Start the server
  echo "Testing server start"
  bin/metastore start --conf datanucleus.schema.autoCreateAll=true --conf metastore.schema.verification=false
  PID_FILE=./run/metastore.pid
  find_pid "Starting server"

  # Give it a second to get going
  sleep 30

  # Restart it
  echo "Testing server restart"
  bin/metastore restart --conf datanucleus.schema.autoCreateAll=true --conf metastore.schema.verification=false
  PREVIOUS_PID=$PID
  find_pid "Restarting server"
  [ $PREVIOUS_PID -ne $PID ] || fail "Server did not restart, still has the same pid $PID" 2

  sleep 30

  # Stop it
  echo "Testing server stop"
  bin/metastore stop
  sleep 15
  confirm_no_pid "Stopping server"
}

if [ "${PROJECT_BASE_DIR}x" == "x" ]
then
  echo "You must set PROJECT_BASE_DIR to the base directory for the project before"
  echo "invoking this script."
  exit 1
fi

BIN_DIST=${PROJECT_BASE_DIR}/packaging/target/apache-riven-*-bin.tar.gz

if [ "${BIN_DIST}x" == "x" ] || [ ! -f $BIN_DIST ]
then
  echo "You must build the distribution packages via 'mvn package' or 'mvn install'"
  echo "before invoking this script"
  exit 1
fi

TMP_DIR=/tmp/metastore-deployment-testing

if [ -e $TMP_DIR ]
then
  rm -rf $TMP_DIR || fail "Unable to remove $TMP_DIR, exiting" 1
fi
mkdir $TMP_DIR

test_simple_deployment $TMP_DIR $BIN_DIST