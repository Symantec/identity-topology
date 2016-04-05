package com.symantec.cpe.trident;

import org.apache.log4j.Logger;

import storm.kafka.trident.mapper.TridentTupleToKafkaMapper;
import storm.trident.tuple.TridentTuple;

/**
 * Overriding the default TridentTupleToKafkaMapper as we are trying to build generic Mapper
 *
 */
@SuppressWarnings("serial")
public class TransactionTupleToKafkaMapper implements TridentTupleToKafkaMapper<Object, Object> {

  private static final Logger LOG = Logger.getLogger(TransactionTupleToKafkaMapper.class);
  String fieldName = "bytes"; // Raw bytes for default

  public TransactionTupleToKafkaMapper(String fieldName) {
    this.fieldName = fieldName;
  }

  @Override
  public Object getKeyFromTuple(TridentTuple tuple) {
    try {
      return tuple.getValueByField(this.fieldName);
    } catch (Exception e) {
      LOG.error("Error while getting message from tuple \t" + this.fieldName, e);
    }
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
