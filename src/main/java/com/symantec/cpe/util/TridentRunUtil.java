package com.symantec.cpe.util;
/**
 * Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import storm.trident.TridentTopology;

/**
 * Run the Topology Remotely or locally
 *
 */
public final class TridentRunUtil {

  /** The Constant LOG. */
  private static final Logger LOG = Logger.getLogger(TridentRunUtil.class);

  private TridentRunUtil() {}

  public static LocalCluster runTopologyLocally(TridentTopology topology, String topologyName,
      Config conf) {
    LocalCluster cluster = new LocalCluster();
    LOG.info("Started Running Topology Locally :" + topologyName);
    cluster.submitTopology(topologyName, conf, topology.build());
    LOG.info(" Topology submitted :" + topologyName);
    return cluster;
  }

  public static void runTopologyRemotely(TridentTopology topology, String topologyName,
      Config conf) {
    LOG.info("Remote Toplogy Submission Started :" + topologyName);
    try {
      StormSubmitter.submitTopology(topologyName, conf, topology.build());
    } catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
      LOG.error("Error while Running toplogy Remotely: ", e);
    }
    LOG.info("Finished Remote Topology Submission :" + topologyName);
  }
}