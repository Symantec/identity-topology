package com.symantec.cpe.trident.mapper;

import org.apache.log4j.Logger;
import org.apache.storm.trident.tuple.TridentTuple;

import symantec.trident.com.bolt.TridentTupleToRabbitMqMapper;

@SuppressWarnings("rawtypes")
public class KafkaToRabbitMqTransactionTupleMapper implements TridentTupleToRabbitMqMapper {

  /**
   * 
   */
  private static final long serialVersionUID = 1983002327508099620L;
  private static final Logger LOG = Logger.getLogger(KafkaToRabbitMqTransactionTupleMapper.class);

  String fieldName = "bytes"; // Raw bytes for default



  public KafkaToRabbitMqTransactionTupleMapper(String partitionFieldName) {
    this.fieldName = partitionFieldName;
  }

  @Override
  public Object getKeyFromTuple(TridentTuple tuple) {
    // TODO Auto-generated method stub
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
