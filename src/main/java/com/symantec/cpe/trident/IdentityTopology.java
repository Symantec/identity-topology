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
package com.symantec.cpe.trident;
 
import java.util.Properties;

import org.apache.log4j.Logger;

import com.symantec.cpe.config.Constants;
import com.symantec.cpe.util.TridentRunUtil;

import backtype.storm.Config;
import backtype.storm.metric.LoggingMetricsConsumer;
import backtype.storm.tuple.Fields;
import storm.kafka.trident.TridentKafkaState;
import storm.kafka.trident.TridentKafkaStateFactory;
import storm.kafka.trident.TridentKafkaUpdater;
import storm.kafka.trident.selector.DefaultTopicSelector;
import storm.trident.Stream;
import storm.trident.TridentState;
import storm.trident.TridentTopology;


public class IdentityTopology {

  private static final Logger LOG = Logger.getLogger(IdentityTopology.class);

 
  /**
   * Builds the Config for producer via KafkaURL, Encoding type and acknowledge count
   * @param brokerURL
   * @param serializerEncodingValue
   * @param requiredAcks
   * @param logginParallelism
   * @param noOfWorkers
   * @param timeOut
   * @param maxSpout
   * @param maxRetries
   * @return
   */
  private static Config getProducerConf(String brokerURL, String serializerEncodingValue,
      String requiredAcks, int logginParallelism, int noOfWorkers, int timeOut, int maxSpout, String maxRetries) {
    Config conf = new Config();
    conf.setNumWorkers(noOfWorkers);
    conf.registerMetricsConsumer(LoggingMetricsConsumer.class, logginParallelism);
    conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, timeOut);
    conf.setMaxSpoutPending(maxSpout);
    
    // set Kafka producer properties.
    Properties props = new Properties();
    props.put(Constants.PROPERTY_NAME_BROKER_URL, brokerURL);
    props.put(Constants.PROPERTY_NAME_REQUIRED_ACKS, requiredAcks);
    props.put(Constants.MAX_RETRIES, maxRetries);
    props.put(Constants.SERIALIZER_CLASS, serializerEncodingValue);

    // put that also in conf
    conf.put(TridentKafkaState.KAFKA_BROKER_PROPERTIES, props);

    return conf;
  }

  /**
   * Reads inputParameters from inputPropertyConf and runs the topology either locally or remotely
   *  
   * @param inputPropertyConf
   */
  public static void buildToplogyAndSubmit(Config inputPropertyConf) {

    String writingBrokerURL = null; /// with port
    String outputTopic = null;
    String topologyName = null;
    String partitionFieldName = null;
    String serializerEncodingValue = null;
    String requiredAcks = null;
    int spoutParallelHint = 1;
    int boltParallelHint = 1;
    int metricsParallel = 1;
    int noOfWorkers = 1;
    int timeOut = 30;
    int maxSpout = 10;
    String maxRetries = "0";

    boolean runLocally = false;
    String spoutType = "kafka";

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


      LOG.info(Constants.DESTINATION_KAFKA_URL_STRING);
      writingBrokerURL = inputPropertyConf.get(Constants.DESTINATION_KAFKA_URL_STRING).toString(); /// with
      LOG.info(writingBrokerURL);

      LOG.info(Constants.OUTPUT_TOPIC_STRING);/// port
      outputTopic = inputPropertyConf.get(Constants.OUTPUT_TOPIC_STRING).toString();
      LOG.info(outputTopic);

      LOG.info(Constants.TOPOLOGY_NAME_STRING);
      topologyName = inputPropertyConf.get(Constants.TOPOLOGY_NAME_STRING).toString();
      LOG.info(topologyName);

      LOG.info(Constants.PARTITION_FIELD_NAME_STRING);
      partitionFieldName = inputPropertyConf.get(Constants.PARTITION_FIELD_NAME_STRING).toString();
      LOG.info(partitionFieldName);

      LOG.info(Constants.ENCODING_STRING);
      serializerEncodingValue = inputPropertyConf.get(Constants.ENCODING_STRING).toString();
      LOG.info(serializerEncodingValue);

      LOG.info(Constants.ACKS_STRING);
      requiredAcks = inputPropertyConf.get(Constants.ACKS_STRING).toString();
      LOG.info(requiredAcks);

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
      
      LOG.info(Constants.MAX_RETRIES);
      maxRetries =
           inputPropertyConf.get(Constants.MAX_RETRIES).toString();
      LOG.info(maxSpout);

    } catch (Exception e) {
      LOG.error("Error in processing property file" + e);
      System.exit(0);
    }

    // Topology Constructor
    TridentTopology topology = new TridentTopology();

    // Producer Config
    Config producerconf = getProducerConf(writingBrokerURL, serializerEncodingValue, requiredAcks,
        metricsParallel, noOfWorkers, timeOut, maxSpout,maxRetries);

    producerconf.setDebug(true);
    // Build Stream with Spout for reading
    Stream stream = StreamBuilder.getStream(spoutType, inputPropertyConf, topology)
        .parallelismHint(spoutParallelHint);

    // Write to endPoint
    TridentKafkaStateFactory stateFactory =
        new TridentKafkaStateFactory().withKafkaTopicSelector(new DefaultTopicSelector(outputTopic))
            .withTridentTupleToKafkaMapper(new TransactionTupleToKafkaMapper(partitionFieldName));

    @SuppressWarnings("unused")
    TridentState state =
        stream.shuffle().partitionPersist(stateFactory, new Fields(partitionFieldName),
            new TridentKafkaUpdater(), new Fields()).parallelismHint(boltParallelHint);
    

    // Submit Topology
    if (runLocally) {
      TridentRunUtil.runTopologyLocally(topology, topologyName, producerconf);
    } else {
      TridentRunUtil.runTopologyRemotely(topology, topologyName, producerconf);
    }
  }

}
