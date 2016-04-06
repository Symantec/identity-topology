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
package com.symantec.cpe.config;
/**
 * Constant variables list
 *
 */
public class Constants {
  public static final String RUN_LOCATION = "run.location";
  public static final String SOURCE_TYPE = "source.type";
  public static final String PROPERTY_NAME_BROKER_URL = "metadata.broker.list";
  public static final String PROPERTY_NAME_REQUIRED_ACKS = "request.required.acks";
  public static final String PROPERTY_NAME_SERIALIZER_CLASS = "serializer.class";
  public static final String RAW_STRING = "raw";
  public static final String DESTINATION_KAFKA_URL_STRING = "destinationKafkaURL";
  public static final String OUTPUT_TOPIC_STRING = "outputTopic";
  public static final String INPUT_TOPIC_STRING = "inputTopic";
  public static final String TOPOLOGY_NAME_STRING = "topologyName";
  public static final String PARTITION_FIELD_NAME_STRING = "partitionFieldName";
  public static final String ENCODING_STRING = "serializerEncodingValue";
  public static final String ACKS_STRING = "requiredAcks";
  public static final String SPOUT_PARALLEL_STRING = "spoutParallelCount";
  public static final String BOLT_PARALLEL_STRING = "boltParallelCount";
  public static final String PARALLEL_METRICS_STRING = "metricsParallelCount";

  public static final String STREAM_NAME_STRING = "streamName";
  public static final String SOURCE_ZOOKEEPER_URL_STRING = "sourceZooKeeperURL";
  public static final String SCHEME_TYPE_STRING = "schemeType";

  public static final String RABBIT_PORT = "rabbitmq.port";
  public static final String RABBIT_HOST = "rabbitmq.host";
  public static final String RABBIT_USERNAME = "rabbitmq.username";
  public static final String RABBIT_PASSWORD = "rabbitmq.password";
  public static final String RABBIT_PREFETCHCOUNT = "rabbitmq.prefetchCount";
  public static final String RABBIT_HA_HOST = "rabbitmq.ha.hosts";
  public static final String RABBIT_REQUEUE = "rabbitmq.requeueOnFail";
  public static final String RABBIT_QUEUENAME = "rabbitmq.queueName";
  
  public static final String RABBITMQ_SHARDCOUNT = "rabbitmq.shard.count";
  
  public static final String RABBITMQ_VIRTUALHOST = "rabbitmq.virtualhost";

  public static final String MAX_BATCH_SIZE = "topology.spout.max.batch.size";
  public static final String MAX_RETRIES = "message.send.max.retries";
  
  public static final String SERIALIZER_CLASS = "serializer.class";
  
  

}
