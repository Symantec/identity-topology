package com.symantec.cpe.trident.mapper;

import org.apache.log4j.Logger;
import org.apache.storm.kafka.trident.mapper.TridentTupleToKafkaMapper;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * Overriding the default TridentTupleToKafkaMapper as we are trying to build generic Mapper
 *
 */
@SuppressWarnings("serial")
public class RabbitMqToKafkaTransactionTupleMapper implements TridentTupleToKafkaMapper<Object, Object> {

  private static final Logger LOG = Logger.getLogger(RabbitMqToKafkaTransactionTupleMapper.class);
  String fieldName = "bytes"; // Raw bytes for default

  public RabbitMqToKafkaTransactionTupleMapper(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public Object getKeyFromTuple(TridentTuple tuple) {
    // RabbitMq doesnot have any Key in the messages.
    return null;
  }

  @Override
  public Object getMessageFromTuple(TridentTuple tuple) {
    try {
      return tuple.getValueByField(this.fieldName);
    } catch (Exception e) {
      LOG.error("Error while getting message from tuple \t" + this.fieldName, e);
    }
    return null;

  }

}
