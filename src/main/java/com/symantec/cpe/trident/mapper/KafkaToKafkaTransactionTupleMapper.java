package com.symantec.cpe.trident.mapper;

import org.apache.log4j.Logger;
import org.apache.storm.kafka.trident.mapper.TridentTupleToKafkaMapper;
import org.apache.storm.trident.tuple.TridentTuple;

/**
 * Overriding the default TridentTupleToKafkaMapper as we are trying to build generic Mapper
 *
 */
@SuppressWarnings("serial")
public class KafkaToKafkaTransactionTupleMapper
    implements TridentTupleToKafkaMapper<Object, Object> {

  private static final Logger LOG = Logger.getLogger(KafkaToKafkaTransactionTupleMapper.class);


  @Override
  public Object getKeyFromTuple(TridentTuple tuple) {
    try {
      return tuple.getValueByField("key");
    } catch (Exception e) {
      LOG.error("Error while getting message from tuple \t" + "key", e);
    }
    return null;
  }

  @Override
  public Object getMessageFromTuple(TridentTuple tuple) {
    try {
      LOG.debug(tuple.toString());
      return tuple.getValueByField("value");
    } catch (Exception e) {
      LOG.error("Error while getting message from tuple \t" + "value", e);
    }
    return null;

  }

}
