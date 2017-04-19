package com.symantec.cpe.bolt.kafka;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.kafka.trident.TridentKafkaStateFactory;
import org.apache.storm.kafka.trident.TridentKafkaUpdater;
import org.apache.storm.kafka.trident.selector.DefaultTopicSelector;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.tuple.Fields;

import com.symantec.cpe.config.Constants;
import com.symantec.cpe.trident.mapper.KafkaToKafkaTransactionTupleMapper;
import com.symantec.cpe.trident.mapper.RabbitMqToKafkaTransactionTupleMapper;

public class BuildKafkaState {

  private static final Logger LOG = Logger.getLogger(BuildKafkaState.class);

  public static TridentState getState(String spoutType, Stream stream, String partitionFieldName,
      Config inputPropertyConf) {

     Properties producerProperties = KafkaProducerConfiguration.getProducerConf(inputPropertyConf);

   

    String outputTopic = inputPropertyConf.get(Constants.OUTPUT_TOPIC_STRING).toString();
    LOG.info("OutputTopic " + outputTopic);

    TridentState state = null;
    TridentKafkaStateFactory stateFactory = null;
    // Write to endPoint
    if (spoutType.toLowerCase().contains("rabbit")) {
      stateFactory = new TridentKafkaStateFactory()
          .withKafkaTopicSelector(new DefaultTopicSelector(outputTopic))
          .withTridentTupleToKafkaMapper(
              new RabbitMqToKafkaTransactionTupleMapper(partitionFieldName));



      LOG.info("Kafka Bolt , input is Rabbit, outputTopic ");
    } else {
      stateFactory = new TridentKafkaStateFactory().withProducerProperties(producerProperties)
          .withKafkaTopicSelector(new DefaultTopicSelector(outputTopic))
          .withTridentTupleToKafkaMapper(
              new KafkaToKafkaTransactionTupleMapper());


      LOG.info("Kafka Bolt , input is Kafka, outputTopic " + stateFactory.getClass());



    }

    // TridentKafkaUpdate
    state = stream.localOrShuffle().partitionPersist(stateFactory, new Fields("value","key"),
        new TridentKafkaUpdater(), new Fields());



    return state;

  }



}
