package com.symantec.cpe.trident;



import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.metric.LoggingMetricsConsumer;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;

import com.symantec.cpe.bolt.kafka.BuildKafkaState;
import com.symantec.cpe.bolt.rabbit.BuildRabbitState;
import com.symantec.cpe.config.Constants;
import com.symantec.cpe.spout.StreamBuilder;
import com.symantec.cpe.util.TridentRunUtil;
import com.symantec.storm.metrics.statsd.StatsdMetricConsumer;


public class IdentityTopology {

  private static final Logger LOG = Logger.getLogger(IdentityTopology.class);


  /**
   * Reads basic inputParameters from inputPropertyConf and runs the topology either locally or
   * remotely
   * 
   * @param inputPropertyConf
   */

  public static void buildToplogyAndSubmit(Config inputPropertyConf) {

    String topologyName = null;
    String partitionFieldName = null;


    String rateLimitFeature = "no";

    int spoutParallelHint = 1;
    int boltParallelHint = 1;
    int metricsParallel = 1;
    int noOfWorkers = 1;
    int timeOut = 30;
    int maxSpout = 1;


    boolean runLocally = false;
    String spoutType = "kafka";
    String boltType = "kafka";

    try {
      if (inputPropertyConf == null || inputPropertyConf.isEmpty()) {
        LOG.error("Error is loading property file" + inputPropertyConf);
        return;
      }


      LOG.info(Constants.RUN_LOCATION);
      LOG.info(inputPropertyConf.get(Constants.RUN_LOCATION).toString());
      LOG.info("Local? \t " + runLocally);
      if (inputPropertyConf.get(Constants.RUN_LOCATION).toString().toLowerCase()
          .contains("remote")) {
        runLocally = false;
      } else {
        runLocally = true;
      }
      LOG.info(runLocally);


      LOG.info(Constants.SOURCE_TYPE);
      spoutType = inputPropertyConf.get(Constants.SOURCE_TYPE).toString(); /// with
      LOG.info(spoutType);



      LOG.info(Constants.TOPOLOGY_NAME_STRING);
      topologyName = inputPropertyConf.get(Constants.TOPOLOGY_NAME_STRING).toString();
      LOG.info(topologyName);

      LOG.info(Constants.PARTITION_FIELD_NAME_STRING);
      partitionFieldName = inputPropertyConf.get(Constants.PARTITION_FIELD_NAME_STRING).toString();
      LOG.info(partitionFieldName);


      LOG.info(Constants.SPOUT_PARALLEL_STRING);
      spoutParallelHint =
          Integer.parseInt(inputPropertyConf.get(Constants.SPOUT_PARALLEL_STRING).toString());
      LOG.info(spoutParallelHint);

      LOG.info(Constants.BOLT_PARALLEL_STRING);
      boltParallelHint =
          Integer.parseInt(inputPropertyConf.get(Constants.BOLT_PARALLEL_STRING).toString());
      LOG.info(boltParallelHint);

      LOG.info(Constants.PARALLEL_METRICS_STRING);
      metricsParallel =
          Integer.parseInt(inputPropertyConf.get(Constants.PARALLEL_METRICS_STRING).toString());
      LOG.info(metricsParallel);

      LOG.info(Config.TOPOLOGY_WORKERS);
      noOfWorkers = Integer.parseInt(inputPropertyConf.get(Config.TOPOLOGY_WORKERS).toString());
      LOG.info(noOfWorkers);

      LOG.info(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS);
      timeOut =
          Integer.parseInt(inputPropertyConf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS).toString());
      LOG.info(timeOut);

      LOG.info(Config.TOPOLOGY_MAX_SPOUT_PENDING);
      maxSpout =
          Integer.parseInt(inputPropertyConf.get(Config.TOPOLOGY_MAX_SPOUT_PENDING).toString());
      LOG.info(maxSpout);

      // new
      LOG.info(Constants.RATE_LIMIT_FEATURE);
      rateLimitFeature = inputPropertyConf.get(Constants.RATE_LIMIT_FEATURE).toString();
      LOG.info(rateLimitFeature);


      // Bolt type
      LOG.info(Constants.DESTINATION_TYPE);
      boltType = inputPropertyConf.get(Constants.DESTINATION_TYPE).toString(); /// with
      LOG.info(boltType);



    } catch (Exception e) {
      LOG.error("Error in processing property file" + e);
      System.exit(0);
    }



    /////////////////////

    // Topology Constructor
    TridentTopology topology = new TridentTopology();



    // Build Stream with Spout for reading, spoutParallelHint is needed for rate limiting
    Stream stream = StreamBuilder.getStream(spoutType, inputPropertyConf, topology,
        rateLimitFeature, spoutParallelHint,partitionFieldName);

    stream.parallelismHint(spoutParallelHint);


    //

    // Write to endPoint, pass spoutType to identify the Tuple Mapper.

    TridentState state = null;
    
    String field = "word";

    if (boltType.toLowerCase().contains("rabbit")) {

      state = BuildRabbitState.getState(spoutType, stream, field, inputPropertyConf);

    } else {

      state = BuildKafkaState.getState(spoutType, stream, field, inputPropertyConf);

    }

    // hack
    
   
    inputPropertyConf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, maxSpout);
    inputPropertyConf.setMaxSpoutPending(maxSpout); 
    // output tasks
    state.parallelismHint(boltParallelHint);

    // set number of workers
    inputPropertyConf.setNumWorkers(noOfWorkers);
    inputPropertyConf.put(Config.TOPOLOGY_ACKER_EXECUTORS, noOfWorkers);

    // timeout
    inputPropertyConf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, timeOut);

    inputPropertyConf.setNumAckers(metricsParallel);
    //
    inputPropertyConf.registerMetricsConsumer(LoggingMetricsConsumer.class, metricsParallel);

    

    
    // Configure the StatsdMetricConsumer
  //  registerStatsDConfig(inputPropertyConf, metricsParallel);

//     inputPropertyConf.setDebug(true);
    // Submit Topology
    if (runLocally) {
      TridentRunUtil.runTopologyLocally(topology, topologyName, inputPropertyConf);
    } else {
      TridentRunUtil.runTopologyRemotely(topology, topologyName, inputPropertyConf);
    }


  }


  private static void registerStatsDConfig(Config inputPropertyConf, int metricsParallel) {
    if (inputPropertyConf.containsKey(Constants.STATSD_FEATURE)) {
      String statsD = inputPropertyConf.get(Constants.STATSD_FEATURE).toString();
      if ("YES".equalsIgnoreCase(statsD)) {
        inputPropertyConf.registerMetricsConsumer(StatsdMetricConsumer.class, inputPropertyConf,
            metricsParallel);
      }

    }
  }
}


