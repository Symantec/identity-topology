package com.symantec.cpe.config.DO;
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

import com.symantec.cpe.config.Constants;

import backtype.storm.Config;

public class RabbitConnectionConfigDO {

  private int id;
  private Config config;
  private String queueName;

  private static final Logger LOG = Logger.getLogger(RabbitConnectionConfigDO.class);

  public RabbitConnectionConfigDO(int i, Config inputPropertyConf) {
    this.id = i;
    this.config = new Config();
    this.queueName = "Rabbit"; // while loadConfig the name is updated

    loadConfig(inputPropertyConf);
  }


  private void loadConfig(Config inputPropertyConf) {
    this.setQueueName(inputPropertyConf.get(Constants.RABBIT_QUEUENAME + this.id).toString());
    this.config.put(Constants.RABBIT_QUEUENAME,
        inputPropertyConf.get(Constants.RABBIT_QUEUENAME + this.id).toString());

    LOG.info(Constants.RABBIT_QUEUENAME + ":" + this.getQueueName());

    this.config.put(Constants.RABBIT_HOST,
        inputPropertyConf.get(Constants.RABBIT_HOST + this.id).toString());
    LOG.info(Constants.RABBIT_HOST + ":" + this.config.get(Constants.RABBIT_HOST));

    this.config.put(Constants.RABBIT_PORT,
        inputPropertyConf.get(Constants.RABBIT_PORT + this.id).toString());
    LOG.info(Constants.RABBIT_PORT + ":" + this.config.get(Constants.RABBIT_PORT));

    this.config.put(Constants.RABBIT_USERNAME,
        inputPropertyConf.get(Constants.RABBIT_USERNAME + this.id).toString());
    LOG.info(Constants.RABBIT_USERNAME + ":" + this.config.get(Constants.RABBIT_USERNAME));

    this.config.put(Constants.RABBIT_PASSWORD,
        inputPropertyConf.get(Constants.RABBIT_PASSWORD + this.id).toString());
    LOG.info(Constants.RABBIT_PASSWORD + ":" + this.config.get(Constants.RABBIT_PASSWORD));

    this.config.put(Constants.RABBIT_PREFETCHCOUNT,
        inputPropertyConf.get(Constants.RABBIT_PREFETCHCOUNT + this.id).toString());
    LOG.info(
        Constants.RABBIT_PREFETCHCOUNT + ":" + this.config.get(Constants.RABBIT_PREFETCHCOUNT));

    this.config.put(Constants.RABBIT_HA_HOST,
        inputPropertyConf.get(Constants.RABBIT_HA_HOST + this.id).toString());
    LOG.info(Constants.RABBIT_HA_HOST + ":" + this.config.get(Constants.RABBIT_HA_HOST));

    this.config.put(Constants.RABBIT_REQUEUE,
        inputPropertyConf.get(Constants.RABBIT_REQUEUE + this.id).toString());
    LOG.info(Constants.RABBIT_REQUEUE + ":" + this.config.get(Constants.RABBIT_REQUEUE));

    this.config.put(Constants.MAX_BATCH_SIZE, inputPropertyConf.get(Constants.MAX_BATCH_SIZE));
    LOG.info(Constants.MAX_BATCH_SIZE + ":" + this.config.get(Constants.MAX_BATCH_SIZE));

    this.config.put(Constants.RABBITMQ_VIRTUALHOST,
        inputPropertyConf.get(Constants.RABBITMQ_VIRTUALHOST+ this.id));
    LOG.info(
        Constants.RABBITMQ_VIRTUALHOST + ":" + this.config.get(Constants.RABBITMQ_VIRTUALHOST));

    // standard # Read internally by RabbitMq spout
    this.config.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS,
        inputPropertyConf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS));
    LOG.info(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS + ":"
        + this.config.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS));
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }


  public String getQueueName() {
    return queueName;
  }


  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }


}
