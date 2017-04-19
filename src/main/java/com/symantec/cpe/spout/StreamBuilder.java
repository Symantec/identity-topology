package com.symantec.cpe.spout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.common.protocol.SecurityProtocol;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutConfig.Builder;
import org.apache.storm.kafka.spout.trident.KafkaTridentSpoutOpaque;
import org.apache.storm.spout.Scheme;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;

import com.symantec.cpe.config.Constants;
import com.symantec.cpe.config.DO.RabbitConnectionConfigDO;
import com.symantec.cpe.config.DO.RateLimitConfigDO;
import com.symantec.cpe.config.DO.RateType;
import com.symantec.cpe.ratelimit.RateLimiterPercentage;
import com.symantec.cpe.ratelimit.RateLimiterValue;

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
  @SuppressWarnings("rawtypes")
  private static Stream getOpaqueTridentKafkaStream(TridentTopology topology,
      Config inputPropertyConf) {

    String streamName = null;
    String inputZookeeperURL = null;
    String inputBrokerId = null;
    String inputTopicNameKafka = null;
    String schemeType = null;
    // defaults
    int bufferSizeBytes = 1024 * 1024;
    int fetchSizeBytes = 1024 * 1024;
    String sslTruststoreLocation = null;
    String sslTruststoreKey = null;
    String sslKeystoreLocation = null;
    String sslKeystoreKey = null;
    String sslKey = null;

    try {
      if (inputPropertyConf == null || inputPropertyConf.isEmpty()) {
        LOG.error("Error is loading property file" + inputPropertyConf);
        return null;
      }



      streamName = inputPropertyConf.get(Constants.STREAM_NAME_STRING).toString();
      inputZookeeperURL = inputPropertyConf.get(Constants.SOURCE_ZOOKEEPER_URL_STRING).toString();;
      inputBrokerId = inputPropertyConf.get(Constants.SOURCE_BROKER_ID).toString();
      inputTopicNameKafka = inputPropertyConf.get(Constants.INPUT_TOPIC_STRING).toString();
      schemeType = inputPropertyConf.get(Constants.SCHEME_TYPE_STRING).toString();
      bufferSizeBytes =
          Integer.parseInt(inputPropertyConf.get(Constants.CONSUMER_FETCH_BUFFER_SIZE).toString());
      fetchSizeBytes =
          Integer.parseInt(inputPropertyConf.get(Constants.CONSUMER_FETCH_SIZE).toString());
      try {
        if (inputPropertyConf.containsKey(Constants.SPOUT_SSL_TRUSTSTORE_LOCATION)
            && inputPropertyConf.containsKey(Constants.SPOUT_SSL_TRUSTSTORE_PASSWORD)) {
          sslTruststoreKey =
              inputPropertyConf.get(Constants.SPOUT_SSL_TRUSTSTORE_PASSWORD).toString();
          sslTruststoreLocation =
              inputPropertyConf.get(Constants.SPOUT_SSL_TRUSTSTORE_LOCATION).toString();
          
          
          // if truststore is good then search for keystore
          
          try {
            if (inputPropertyConf.containsKey(Constants.SPOUT_SSL_KEYSTORE_LOCATION)
                && inputPropertyConf.containsKey(Constants.SPOUT_SSL_KEYSTORE_PASSWORD)
                && inputPropertyConf.containsKey(Constants.SPOUT_SSL_KEY_PASSWORD)) {
              sslKeystoreKey = inputPropertyConf.get(Constants.SPOUT_SSL_KEYSTORE_PASSWORD).toString();
              sslKey = inputPropertyConf.get(Constants.SPOUT_SSL_KEY_PASSWORD).toString();
              sslKeystoreLocation =
                  inputPropertyConf.get(Constants.SPOUT_SSL_KEYSTORE_LOCATION).toString();
            } else {
              LOG.info("KEYSTORE NOT ENABLED");
            }

          } catch (Exception e) {
            LOG.error("Error in processing property file" + e.getMessage());
          }
          
        } else {
          LOG.info("SSL NOT ENABLED");
        }
      } catch (Exception e) {
        LOG.error(String.format("Failed while trying to set SSL config"));
      }

     

    } catch (Exception e) {
      LOG.error("Error in processing property file" + e.getMessage());
    }
    Class<? extends Deserializer<?>> deSerializer = SchemeBuilder.getDeserializer(schemeType);


    KafkaTridentSpoutOpaque spout = getKafkaSpout(inputBrokerId, deSerializer, inputZookeeperURL,
        inputTopicNameKafka, streamName, bufferSizeBytes, fetchSizeBytes, topology,
        sslTruststoreLocation, sslTruststoreKey, sslKeystoreLocation, sslKeystoreKey, sslKey);
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
  // kk = org.apache.kafka.common.serialization.StringDeserializer.class;
  @SuppressWarnings("rawtypes")
  private static KafkaTridentSpoutOpaque getKafkaSpout(String bootStrapServer,
      Class<? extends Deserializer<?>> deSerializer, String zookeeperURL, String topicName,
      String streamName, int bufferSizeBytes, int fetchSizeBytes, TridentTopology topology,
      String sslTruststoreLocation, String sslTruststoreKey, String sslKeystoreLocation,
      String sslKeystoreKey, String sslKey) {


    Builder<String, String> config =
        KafkaSpoutConfig.builder(bootStrapServer, Pattern.compile(topicName));

    config.setBootstrapServers(bootStrapServer);
    // hardcoded, TODO
    config.setFirstPollOffsetStrategy(
        org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST);
    config.setGroupId(streamName);
    config.setMaxPartitionFectchBytes(fetchSizeBytes);
    config.setKey(org.apache.kafka.common.serialization.StringDeserializer.class);
    config.setValue(org.apache.kafka.common.serialization.StringDeserializer.class);
    if (sslTruststoreLocation != null && sslTruststoreKey != null
        && sslTruststoreLocation.trim().length() > 0 && sslTruststoreKey.trim().length() > 0) {
      config.setSecurityProtocol(SecurityProtocol.SSL.name);
      config.setSSLTruststore(sslTruststoreLocation.trim(), sslTruststoreKey.trim());
      
      
      // if trustore then keystore
      if (sslKeystoreLocation != null && sslKeystoreKey != null && sslKey != null
          && sslKeystoreLocation.length() > 0 && sslKeystoreKey.length() > 0 && sslKey.length() > 0) {
        config.setSSLKeystore(sslKeystoreLocation, sslKeystoreKey, sslKey);
      }
    }
    

    return new KafkaTridentSpoutOpaque<>(config.build());
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
      shardCount =
          Integer.parseInt(inputPropertyConf.get(Constants.RABBITMQ_SHARDCOUNT).toString());
      for (int i = 0; i < shardCount; i++) {
        RabbitConnectionConfigDO configDO =
            new RabbitConnectionConfigDO("input", i, inputPropertyConf);
        queueConfigList.add(configDO);
      }
    } catch (Exception e) {
      LOG.error("Error in processing property file" + e);
    }

    List<Stream> streams = new ArrayList<Stream>();
    for (int i = 0; i < queueConfigList.size(); i++) {
      Scheme scheme = SchemeBuilder.getScheme(schemeType);
      RabbitConnectionConfigDO configDO = queueConfigList.get(i);
      @SuppressWarnings("unchecked")
      TridentRabbitMqSpout spout =
          getRabbitMqTridentSpout(scheme, configDO.getQueueName(), configDO.getConfig());
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
      Map<String, Object> producerConf) {
    TridentRabbitMqSpout spout = new TridentRabbitMqSpout(scheme, streamId, producerConf);
    LOG.info("Finished Building RabbitMQSpout");
    return spout;
  }



  /**
   * Builds the Stream with required Spout
   * 
   * @return
   */
  public static Stream getStream(String spoutType, Config inputPropertyConf,
      TridentTopology topology, String partitionFieldName) {

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

  /**
   * Builds the Stream with required Spout
   * 
   * @return
   */
  public static Stream getStream(String spoutType, Config inputPropertyConf,
      TridentTopology topology, String rateLimitFeature, int spoutParallelHint,
      String partitionFieldName) {

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

    Stream stream = null;

    if (spoutType.toLowerCase().contains("rabbit")) {
      stream = getRabbitMqTridentStream(topology, inputPropertyConf);
    } else {
      stream = getOpaqueTridentKafkaStream(topology, inputPropertyConf);
    }

    // only do this is rate limiting is enabled
    if (rateLimitFeature != null && !rateLimitFeature.isEmpty()
        && (rateLimitFeature.compareToIgnoreCase("YES") == 0)) {
      stream = updateStream(stream, spoutParallelHint, inputPropertyConf);
    } else {
      // do nothing
      LOG.info("No Rate Limiting");
    }
    return stream;

  }



  //////////// Rate limiting Logic Below

  private static Stream updateStream(Stream stream, int spoutParallelHint,
      Config inputPropertyConf) {

    // new Stream
    Stream updateStream = null;


    // Build the RateLimitDO
    RateLimitConfigDO rateLimitConfig = buildRateParameters(inputPropertyConf);

    if (rateLimitConfig.getRateType().equals(RateType.PERCENTAGE)) {
      updateStream =
          stream.each(stream.getOutputFields(), new RateLimiterPercentage(rateLimitConfig));

    } else {
      updateStream = stream.each(stream.getOutputFields(),
          new RateLimiterValue(rateLimitConfig, spoutParallelHint));

    }

    return updateStream;


  }

  private static RateLimitConfigDO buildRateParameters(Config inputPropertyConf) {
    LOG.info("Trying to Read Rate Parameters");
    RateLimitConfigDO rateLimit = new RateLimitConfigDO();
    try {
      LOG.info(Constants.RATE_LIMIT_TYPE);
      rateLimit.setRateType(inputPropertyConf.get(Constants.RATE_LIMIT_TYPE).toString());
      LOG.info(inputPropertyConf.get(Constants.RATE_LIMIT_TYPE).toString());

      LOG.info(Constants.RATE_PERCENTAGE);
      rateLimit.setRatePercentage(
          Integer.parseInt(inputPropertyConf.get(Constants.RATE_PERCENTAGE).toString()));
      LOG.info(inputPropertyConf.get(Constants.RATE_PERCENTAGE).toString());


      LOG.info(Constants.RATE_VALUE);
      rateLimit
          .setRateValue(Long.parseLong(inputPropertyConf.get(Constants.RATE_VALUE).toString()));
      LOG.info(inputPropertyConf.get(Constants.RATE_VALUE).toString());

    } catch (Exception e) {
      LOG.error("Error while reading Rate Limit Parameters ");
    }
    return rateLimit;


  }



}
