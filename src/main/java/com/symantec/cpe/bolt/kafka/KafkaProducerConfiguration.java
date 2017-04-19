package com.symantec.cpe.bolt.kafka;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.protocol.SecurityProtocol;
import org.apache.log4j.Logger;
import org.apache.storm.Config;

import com.symantec.cpe.config.Constants;

public class KafkaProducerConfiguration {


  private static final Logger LOG = Logger.getLogger(KafkaProducerConfiguration.class);

  /**
   * Builds the Config for producer via KafkaURL, Encoding type and acknowledge count
   * 
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
  public static Properties getProducerConf(String brokerURL, String serializerEncodingValue,
      String requiredAcks, int maxSpout, String maxRetries, int batchsize, int lingerMS,
      int bufferMemory, String sslTruststoreKey, String sslTruststoreLocation,
      String sslKeystoreLocation, String sslKeystoreKey, String sslKey) {


    // set Kafka producer properties.
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerURL);
    props.put(Constants.PROPERTY_NAME_REQUIRED_ACKS, requiredAcks);
    props.put(Constants.MAX_RETRIES, maxRetries);
    props.put(Constants.BATCH_SIZE, batchsize);
    props.put(Constants.LINGER_MS, lingerMS);
    props.put(Constants.BUFFER_MEMORY, bufferMemory);
    props.setProperty(KEY_SERIALIZER_CLASS_CONFIG, serializerEncodingValue);
    props.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, serializerEncodingValue);


    if (sslTruststoreKey != null && sslTruststoreLocation != null && sslTruststoreKey.length() > 0
        && sslTruststoreLocation.length() > 0) {
      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SSL.name);
      props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
      props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststoreKey);

      // if trustore then keystore
      if (sslKeystoreLocation != null && sslKeystoreKey != null && sslKey != null
          && sslKeystoreLocation.length() > 0 && sslKeystoreKey.length() > 0
          && sslKey.length() > 0) {
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, sslKeystoreLocation);
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslKeystoreKey);
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslKey);
      } else {
        LOG.info("SSL KEYSTORE NOT enabled");
      }
    } else {
      LOG.info("SSL NOT ENABLED");
    }



    return props;
  }

  public static Properties getProducerConf(Config inputPropertyConf) {

    String maxRetries = "0";
    String writingBrokerURL = null; /// with port
    String outputTopic = null;
    int maxSpout = 0;
    String requiredAcks = null;
    String serializerEncodingValue = null;
    int batchSize = 16834;
    int lingerMS = 1;
    int bufferMemory = 1024000;

    String sslTruststoreKey = null;
    String sslTruststoreLocation = null;

    String sslKeystoreLocation = null;
    String sslKeystoreKey = null;
    String sslKey = null;

    LOG.info(Constants.ACKS_STRING);
    requiredAcks = inputPropertyConf.get(Constants.ACKS_STRING).toString();
    LOG.info(requiredAcks);

    LOG.info(Constants.DESTINATION_KAFKA_URL_STRING);
    writingBrokerURL = inputPropertyConf.get(Constants.DESTINATION_KAFKA_URL_STRING).toString(); /// with
    LOG.info(writingBrokerURL);


    LOG.info(Constants.ENCODING_STRING);
    serializerEncodingValue = inputPropertyConf.get(Constants.ENCODING_STRING).toString();

    LOG.info(serializerEncodingValue);



    LOG.info(Constants.MAX_RETRIES);
    maxRetries = inputPropertyConf.get(Constants.MAX_RETRIES).toString();
    LOG.info(maxSpout);


    LOG.info(Constants.OUTPUT_TOPIC_STRING);/// port
    outputTopic = inputPropertyConf.get(Constants.OUTPUT_TOPIC_STRING).toString();
    LOG.info(outputTopic);


    LOG.info(Constants.BATCH_SIZE);
    batchSize = Integer.parseInt(inputPropertyConf.get(Constants.BATCH_SIZE).toString());
    LOG.info(batchSize);

    LOG.info(Constants.LINGER_MS);
    lingerMS = Integer.parseInt(inputPropertyConf.get(Constants.LINGER_MS).toString());
    LOG.info(lingerMS);

    LOG.info(Constants.BUFFER_MEMORY);
    bufferMemory = Integer.parseInt(inputPropertyConf.get(Constants.BUFFER_MEMORY).toString());
    LOG.info(bufferMemory);

    try {
      if (inputPropertyConf.containsKey(Constants.BOLT_SSL_TRUSTSTORE_LOCATION)
          && inputPropertyConf.containsKey(Constants.BOLT_SSL_TRUSTSTORE_PASSWORD)) {
        LOG.info(Constants.BOLT_SSL_TRUSTSTORE_PASSWORD);
        sslTruststoreKey = inputPropertyConf.get(Constants.BOLT_SSL_TRUSTSTORE_PASSWORD).toString();
        LOG.info(sslTruststoreKey);
        LOG.info(Constants.BOLT_SSL_TRUSTSTORE_LOCATION);
        sslTruststoreLocation =
            inputPropertyConf.get(Constants.BOLT_SSL_TRUSTSTORE_LOCATION).toString();
        LOG.info(sslTruststoreLocation);

        // if truststore is good then search for keystore

        try {
          if (inputPropertyConf.containsKey(Constants.BOLT_SSL_KEYSTORE_LOCATION)
              && inputPropertyConf.containsKey(Constants.BOLT_SSL_KEYSTORE_PASSWORD)
              && inputPropertyConf.containsKey(Constants.BOLT_SSL_KEY_PASSWORD)) {
            sslKeystoreKey = inputPropertyConf.get(Constants.BOLT_SSL_KEYSTORE_PASSWORD).toString();
            sslKey = inputPropertyConf.get(Constants.BOLT_SSL_KEY_PASSWORD).toString();
            sslKeystoreLocation =
                inputPropertyConf.get(Constants.BOLT_SSL_KEYSTORE_LOCATION).toString();
          } else {
            LOG.info("KEYSTORE NOT ENABLED");
          }

        } catch (Exception e) {
          LOG.error("Exception while handling Keystore configs");
        }

      } else {
        LOG.info("TRUSTSTORE NOT ENABLED");
      }
    } catch (Exception e) {
      LOG.error("Exception while handling SSL configs");
    }

    return getProducerConf(writingBrokerURL, serializerEncodingValue, requiredAcks, maxSpout,
        maxRetries, batchSize, lingerMS, bufferMemory, sslTruststoreKey, sslTruststoreLocation,
        sslKeystoreLocation, sslKeystoreKey, sslKey);
  }

}

