package com.symantec.cpe.config;

import org.apache.kafka.common.config.SslConfigs;

/**
 * Constant variables list
 *
 */
public class Constants {
  public static final String RUN_LOCATION = "run.location";
  public static final String SOURCE_TYPE = "source.type";
  public static final String DESTINATION_TYPE = "destination.type";



  public static final String PROPERTY_NAME_REQUIRED_ACKS = "request.required.acks";
  public static final String PROPERTY_NAME_SERIALIZER_CLASS = "serializer.class";
  public static final String RAW_STRING = "raw";
  public static final String DESTINATION_KAFKA_URL_STRING = "destinationKafkaURL";
  public static final String OUTPUT_TOPIC_STRING = "outputTopic";
  public static final String INPUT_TOPIC_STRING = "inputTopic";
  public static final String TOPOLOGY_NAME_STRING = "topology.name";
  public static final String PARTITION_FIELD_NAME_STRING = "partitionFieldName";
  public static final String ENCODING_STRING = "serializerEncodingValue";
  public static final String ACKS_STRING = "requiredAcks";
  public static final String SPOUT_PARALLEL_STRING = "spoutParallelCount";
  public static final String BOLT_PARALLEL_STRING = "boltParallelCount";
  public static final String PARALLEL_METRICS_STRING = "metricsParallelCount";

  public static final String STREAM_NAME_STRING = "streamName";
  public static final String SOURCE_ZOOKEEPER_URL_STRING = "sourceZooKeeperURL";
  public static final String SOURCE_BROKER_ID = "sourceBrokerId";
  public static final String SCHEME_TYPE_STRING = "schemeType";


  // Rabbit mq
  public static final String RABBIT_PORT = "rabbitmq.port";
  public static final String RABBIT_HOST = "rabbitmq.host";
  public static final String RABBITMQ_VIRTUALHOST = "rabbitmq.virtualhost";

  public static final String RABBIT_USERNAME = "rabbitmq.username";
  public static final String RABBIT_PASSWORD = "rabbitmq.password";

  public static final String RABBIT_PREFETCHCOUNT = "rabbitmq.prefetchCount";

  public static final String RABBIT_HA_HOST = "rabbitmq.ha.hosts";
  public static final String RABBIT_REQUEUE = "rabbitmq.requeueOnFail";
  public static final String RABBIT_QUEUENAME = "rabbitmq.queueName";
  public static final String RABBIT_QUEUE_DURABLE = "rabbitmq.queue.durable";
  public static final String RABBITMQ_SHARDCOUNT = "rabbitmq.shard.count";

  public static final String RABBITMQ_EXCHANGE_NAME = "rabbitmq.exchange.name";
  public static final String RABBITMQ_EXCHANGE_TYPE = "rabbitmq.exchange.type";
  public static final String RABBITMQ_EXCHANGE_DURABLE = "rabbitmq.exchange.durable";
  public static final String RABBITMQ_EXCHANGE_EXCLUSIVE = "rabbitmq.exchange.exclusive";

  public static final String RABBITMQ_ROUTINGKEY = "rabbitmq.routingKey";
  public static final String RABBITMQ_CONTENTTYPE = "rabbitmq.contentType";
  public static final String RABBITMQ_ENCODING = "rabbitmq.encoding";

  public static final String RABBITMQ_HEARTBEAT = "rabbitmq.heartbeat";

  // Kafka Config
  public static final String CONSUMER_FETCH_SIZE = "kafka.consumer.fetch.size.byte";
  public static final String CONSUMER_FETCH_BUFFER_SIZE = "kafka.consumer.buffer.size.byte";
  //


  public static final String MAX_RETRIES = "message.send.max.retries";
  public static final String SERIALIZER_CLASS = "serializer.class";


  // Rate Limiting
  public static final String RATE_LIMIT_FEATURE = "rate.limit";
  public static final String RATE_LIMIT_TYPE = "rate.type";
  public static final String RATE_PERCENTAGE = "percent.limit";
  public static final String RATE_VALUE = "value.limit";

  // StatsD config

  public static final String STATSD_FEATURE = "statsd";
  public static final String STATSD_HOST = "metrics.statsd.host";
  public static final String STATSD_PORT = "metrics.statsd.port";
  public static final String STATSD_PREFIX = "metrics.statsd.prefix";


  // new

  public static final String BATCH_SIZE = "batch.size";
  public static final String LINGER_MS = "linger.ms";
  public static final String BUFFER_MEMORY = "buffer.memory";

  // SSL -trustStore
  public static final String SPOUT_SSL_TRUSTSTORE_LOCATION =
      "spout." + SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
  public static final String SPOUT_SSL_TRUSTSTORE_PASSWORD =
      "spout." + SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;

  public static final String BOLT_SSL_TRUSTSTORE_LOCATION =
      "bolt." + SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
  public static final String BOLT_SSL_TRUSTSTORE_PASSWORD =
      "bolt." + SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
  
  // SSL - keystore 
  
  public static final String SPOUT_SSL_KEYSTORE_LOCATION =
      "spout." + SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
  public static final String SPOUT_SSL_KEYSTORE_PASSWORD =
      "spout." + SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
  public static final String SPOUT_SSL_KEY_PASSWORD =
      "spout." + SslConfigs.SSL_KEY_PASSWORD_CONFIG;

  public static final String BOLT_SSL_KEYSTORE_LOCATION =
      "bolt." + SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
  public static final String BOLT_SSL_KEYSTORE_PASSWORD =
      "bolt." + SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
  public static final String BOLT_SSL_KEY_PASSWORD =
      "bolt." + SslConfigs.SSL_KEY_PASSWORD_CONFIG;
  


}
