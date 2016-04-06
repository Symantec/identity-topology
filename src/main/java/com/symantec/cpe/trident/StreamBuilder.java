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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.symantec.cpe.config.Constants;
import com.symantec.cpe.config.DO.RabbitConnectionConfigDO;

import backtype.storm.Config;
import backtype.storm.spout.Scheme;
import backtype.storm.spout.SchemeAsMultiScheme;
import storm.kafka.BrokerHosts;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import symantec.trident.com.TridentRabbitMqSpout;

public class StreamBuilder {
  private static final Logger LOG = Logger.getLogger(StreamBuilder.class);


  /**
   * 
   * @param topology
   * @param inputPropertyConf
   * @param producerConf
   * @return
   */
  private static Stream getOpaqueTridentKafkaStream(TridentTopology topology,
      Config inputPropertyConf) {

    String streamName = null;
    String inputZookeeperURL = null;
    String inputTopicNameKafka = null;
    String schemeType = null;

    try {
      if (inputPropertyConf == null || inputPropertyConf.isEmpty()) {
        LOG.error("Error is loading property file" + inputPropertyConf);
        return null;
      }

      streamName = inputPropertyConf.get(Constants.STREAM_NAME_STRING).toString();
      inputZookeeperURL = inputPropertyConf.get(Constants.SOURCE_ZOOKEEPER_URL_STRING).toString();;
      inputTopicNameKafka = inputPropertyConf.get(Constants.INPUT_TOPIC_STRING).toString();
      schemeType = inputPropertyConf.get(Constants.SCHEME_TYPE_STRING).toString();

    } catch (Exception e) {
      LOG.error("Error in processing property file" + e.getMessage());
    }
    Scheme scheme = SchemeBuilder.getScheme(schemeType);
    OpaqueTridentKafkaSpout spout = getKafkaSpout(scheme, inputZookeeperURL, inputTopicNameKafka,streamName);
    return topology.newStream(streamName, spout);
  }



  /**
   * Builds the OpaqueTridentKafkaSpout via Zookeeper and Topic Name
   * 
   * @param scheme
   * @param zookeeperURL
   * @param topicName
   * @return
   */
  private static OpaqueTridentKafkaSpout getKafkaSpout(Scheme scheme, String zookeeperURL,
      String topicName,String streamName) {

    BrokerHosts zk = new ZkHosts(zookeeperURL);
    TridentKafkaConfig spoutConf = new TridentKafkaConfig(zk, topicName,streamName);
    spoutConf.scheme = new SchemeAsMultiScheme(scheme);
    spoutConf.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();
    OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(spoutConf);
    LOG.info("Finished Building OpaqueTridentKafkaSpout");
    return spout;
  }



  private static Stream getRabbitMqTridentStream(TridentTopology topology,
      Config inputPropertyConf) {

    String schemeType = null;
    int shardCount = 1;
    List<RabbitConnectionConfigDO> queueConfigList = new ArrayList<>();
    try {
      if (inputPropertyConf == null || inputPropertyConf.isEmpty()) {
        LOG.error("Error is loading property file" + inputPropertyConf);
        return null;
      }

      schemeType = inputPropertyConf.get(Constants.SCHEME_TYPE_STRING).toString();
      shardCount = Integer.parseInt(inputPropertyConf.get(Constants.RABBITMQ_SHARDCOUNT).toString());
      for (int i = 0; i < shardCount; i++) {
        RabbitConnectionConfigDO configDO = new RabbitConnectionConfigDO(i, inputPropertyConf);
        queueConfigList.add(configDO);
      }
    } catch (Exception e) {
      LOG.error("Error in processing property file" + e);
    }

    List<Stream> streams = new ArrayList<Stream>();
    for (int i = 0; i < shardCount; i++) {
      Scheme scheme = SchemeBuilder.getScheme(schemeType);
      RabbitConnectionConfigDO configDO = queueConfigList.get(i);
      TridentRabbitMqSpout spout = getRabbitMqTridentSpout(scheme, configDO.getQueueName(), configDO.getConfig());
      Stream st = topology.newStream(configDO.getQueueName(), spout);
      streams.add(st);
    }
    return topology.merge(streams);

  }


    /**
     * 
     * @param scheme
     * @param producerConf
     * @return
     */
    private static TridentRabbitMqSpout getRabbitMqTridentSpout(Scheme scheme, String streamId,
        Config producerConf) {
      TridentRabbitMqSpout spout = new TridentRabbitMqSpout(scheme, streamId, producerConf);
      LOG.info("Finished Building RabbitMQSpout");
      return spout;
    }

//  /**
//   * 
//   * @param scheme
//   * @param producerConf
//   * @return
//   */
//  private static RabbitMQSpout getRabbitMqTridentSpout(Scheme scheme, String streamId,
//      Config producerConf) {
//    RabbitMQSpout spout = new RabbitMQSpout(scheme, streamId, producerConf);
//
//    LOG.info("Finished Building RabbitMQSpout");
//    return spout;
//  }

  /**
   * Builds the Stream with required Spout
   * 
   * @return
   */
  public static Stream getStream(String spoutType, Config inputPropertyConf,
      TridentTopology topology) {

    if (spoutType == null || spoutType.length() == 0) {
      LOG.error("Input Spout Type is wrong");
      return null;
    }

    try {
      if (inputPropertyConf == null || inputPropertyConf.isEmpty()) {
        LOG.error("Error is loading property file" + inputPropertyConf);
        return null;
      }


    } catch (Exception e) {
      LOG.error("Error in processing property file" + e);
    }


    if (spoutType.toLowerCase().contains("rabbit")) {
      return getRabbitMqTridentStream(topology, inputPropertyConf);
    } else {
      return getOpaqueTridentKafkaStream(topology, inputPropertyConf);
    }
  }



}
