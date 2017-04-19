package com.symantec.cpe.util;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.trident.TridentTopology;

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