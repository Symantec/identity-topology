package com.symantec.cpe.bolt.rabbit;

import org.apache.storm.trident.tuple.TridentTuple;

import symantec.trident.com.bolt.RabbitMqTopicSelector;

public class DefaultTopicSelector implements RabbitMqTopicSelector {
 
  private static final long serialVersionUID = 1L;
    private final String topicName;

    public DefaultTopicSelector(final String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String getTopic(TridentTuple tuple) {
        return topicName;
    }
}
