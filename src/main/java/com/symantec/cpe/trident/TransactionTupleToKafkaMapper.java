package com.symantec.cpe.trident;
/**
 * Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
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
