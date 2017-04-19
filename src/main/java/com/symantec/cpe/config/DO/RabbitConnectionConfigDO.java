package com.symantec.cpe.config.DO;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.trident.spout.RichSpoutBatchExecutor;

import com.symantec.cpe.config.Constants;
import com.symantec.cpe.config.TYPE;


public class RabbitConnectionConfigDO {

  private int id;
  @SuppressWarnings("rawtypes")
  private Map config;
  private String queueName;
  private String type;

  private static final Logger LOG = Logger.getLogger(RabbitConnectionConfigDO.class);



  public RabbitConnectionConfigDO(String typeString, int i, Config inputPropertyConf) {
    this.id = i;
    this.type = TYPE.getType(typeString).toString().toLowerCase() + ".";
    this.config = new Config();
    this.queueName = "Rabbit"; // while loadConfig the name is updated

    // Common Config
    commonRabbitMqConfig(inputPropertyConf);

    // Special
    if (TYPE.INPUT.toString().compareToIgnoreCase(typeString) == 0) {
      loadInputConfigSeparate(inputPropertyConf);
    } else {
      loadOutputConfigSeparate(inputPropertyConf);
    }
    // StatsD
    if(inputPropertyConf.containsKey(Constants.STATSD_FEATURE)){
      String statsD= inputPropertyConf.get(Constants.STATSD_FEATURE).toString();
      if("YES".equalsIgnoreCase(statsD)){
        loadStatsDConfig(inputPropertyConf);
      }
      
    }


  }

  @SuppressWarnings("unchecked")
  private void commonRabbitMqConfig(Config inputPropertyConf) {
    LOG.debug("Starting Common Config");

   
    // topology name
    this.config.put(Constants.TOPOLOGY_NAME_STRING,
        inputPropertyConf.get(Constants.TOPOLOGY_NAME_STRING).toString());

    LOG.info(
        Constants.TOPOLOGY_NAME_STRING + ":" + this.config.get(Constants.TOPOLOGY_NAME_STRING));

    // rabbit

    this.setQueueName(
        inputPropertyConf.get(this.type + Constants.RABBIT_QUEUENAME + this.id).toString());

    this.config.put(Constants.RABBIT_QUEUENAME,
        inputPropertyConf.get(this.type + Constants.RABBIT_QUEUENAME + this.id).toString());

    LOG.info(this.type + Constants.RABBIT_QUEUENAME + ":" + this.getQueueName());

    this.config.put(Constants.RABBIT_HOST,
        inputPropertyConf.get(this.type + Constants.RABBIT_HOST + this.id).toString());
    LOG.info(this.type + Constants.RABBIT_HOST + ":" + this.config.get(Constants.RABBIT_HOST));

    this.config.put(Constants.RABBIT_PORT,
        inputPropertyConf.get(this.type + Constants.RABBIT_PORT + this.id).toString());
    LOG.info(this.type + Constants.RABBIT_PORT + ":" + this.config.get(Constants.RABBIT_PORT));

    this.config.put(Constants.RABBIT_USERNAME,
        inputPropertyConf.get(this.type + Constants.RABBIT_USERNAME + this.id).toString());
    LOG.info(
        this.type + Constants.RABBIT_USERNAME + ":" + this.config.get(Constants.RABBIT_USERNAME));

    this.config.put(Constants.RABBIT_PASSWORD,
        inputPropertyConf.get(this.type + Constants.RABBIT_PASSWORD + this.id).toString());
    LOG.info(
        this.type + Constants.RABBIT_PASSWORD + ":" + this.config.get(Constants.RABBIT_PASSWORD));


    this.config.put(Constants.RABBIT_HA_HOST,
        inputPropertyConf.get(this.type + Constants.RABBIT_HA_HOST + this.id).toString());
    LOG.info(
        this.type + Constants.RABBIT_HA_HOST + ":" + this.config.get(Constants.RABBIT_HA_HOST));


    this.config.put(RichSpoutBatchExecutor.MAX_BATCH_SIZE_CONF,
        inputPropertyConf.get(this.type + RichSpoutBatchExecutor.MAX_BATCH_SIZE_CONF + this.id));
    LOG.info(this.type + RichSpoutBatchExecutor.MAX_BATCH_SIZE_CONF + ":"
        + this.config.get(RichSpoutBatchExecutor.MAX_BATCH_SIZE_CONF + this.id));

    this.config.put(Constants.RABBITMQ_VIRTUALHOST,
        inputPropertyConf.get(this.type + Constants.RABBITMQ_VIRTUALHOST + this.id));
    LOG.info(this.type + Constants.RABBITMQ_VIRTUALHOST + ":"
        + this.config.get(Constants.RABBITMQ_VIRTUALHOST));

    // standard # Read internally by RabbitMq spout
    this.config.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS,
        inputPropertyConf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS));
    LOG.info(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS + ":"
        + this.config.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS));


    // new
    this.config.put(Constants.RABBITMQ_HEARTBEAT,
        inputPropertyConf.get(Constants.RABBITMQ_HEARTBEAT));
    LOG.info(Constants.RABBITMQ_HEARTBEAT + ":" + this.config.get(Constants.RABBITMQ_HEARTBEAT));

    LOG.debug("Ending Common Config");
    
    
    
  }

  @SuppressWarnings("unchecked")
  private void loadInputConfigSeparate(Config inputPropertyConf) {


    this.config.put(Constants.RABBIT_PREFETCHCOUNT,
        inputPropertyConf.get(this.type + Constants.RABBIT_PREFETCHCOUNT + this.id).toString());
    LOG.info(this.type + Constants.RABBIT_PREFETCHCOUNT + ":"
        + this.config.get(Constants.RABBIT_PREFETCHCOUNT));

    this.config.put(Constants.RABBIT_REQUEUE,
        inputPropertyConf.get(this.type + Constants.RABBIT_REQUEUE + this.id).toString());
    LOG.info(
        this.type + Constants.RABBIT_REQUEUE + ":" + this.config.get(Constants.RABBIT_REQUEUE));


  }
  
  @SuppressWarnings("unchecked")
  private void loadStatsDConfig(Config inputPropertyConf){
    LOG.debug(" Starting Specific StatsD COnfig");
    // statsD

    this.config.put(Constants.STATSD_FEATURE, inputPropertyConf.get(Constants.STATSD_FEATURE).toString());

    LOG.info(Constants.STATSD_FEATURE + ":" + this.config.get(Constants.STATSD_FEATURE));
    
    this.config.put(Constants.STATSD_HOST, inputPropertyConf.get(Constants.STATSD_HOST).toString());

    LOG.info(Constants.STATSD_HOST + ":" + this.config.get(Constants.STATSD_HOST));

    this.config.put(Constants.STATSD_PORT, inputPropertyConf.get(Constants.STATSD_PORT).toString());

    LOG.info(Constants.STATSD_PORT + ":" + this.config.get(Constants.STATSD_PORT));

    this.config.put(Constants.STATSD_PREFIX,
        inputPropertyConf.get(Constants.STATSD_PREFIX).toString());

    LOG.info(Constants.STATSD_PREFIX + ":" + this.config.get(Constants.STATSD_PREFIX));
 
    LOG.debug(" Ending Specific StatsD COnfig");
    
  }


  @SuppressWarnings("unchecked")
  private void loadOutputConfigSeparate(Config inputPropertyConf) {
    LOG.debug("Output Starting Specific,Bolt, COnfig");

    this.config.put(Constants.RABBITMQ_EXCHANGE_NAME,
        inputPropertyConf.get(this.type + Constants.RABBITMQ_EXCHANGE_NAME + this.id).toString());
    LOG.info(
        Constants.RABBITMQ_EXCHANGE_NAME + ":" + this.config.get(Constants.RABBITMQ_EXCHANGE_NAME));


    this.config.put(Constants.RABBITMQ_EXCHANGE_TYPE,
        inputPropertyConf.get(this.type + Constants.RABBITMQ_EXCHANGE_TYPE + this.id).toString());
    LOG.info(
        Constants.RABBITMQ_EXCHANGE_TYPE + ":" + this.config.get(Constants.RABBITMQ_EXCHANGE_TYPE));


    this.config.put(Constants.RABBIT_QUEUE_DURABLE,
        inputPropertyConf.get(this.type + Constants.RABBIT_QUEUE_DURABLE + this.id).toString());
    LOG.info(
        Constants.RABBIT_QUEUE_DURABLE + ":" + this.config.get(Constants.RABBIT_QUEUE_DURABLE));


    this.config.put(Constants.RABBITMQ_EXCHANGE_DURABLE, inputPropertyConf
        .get(this.type + Constants.RABBITMQ_EXCHANGE_DURABLE + this.id).toString());
    LOG.info(Constants.RABBITMQ_EXCHANGE_DURABLE + ":"
        + this.config.get(Constants.RABBITMQ_EXCHANGE_DURABLE));

    this.config.put(Constants.RABBITMQ_EXCHANGE_EXCLUSIVE, inputPropertyConf
        .get(this.type + Constants.RABBITMQ_EXCHANGE_EXCLUSIVE + this.id).toString());
    LOG.info(Constants.RABBITMQ_EXCHANGE_EXCLUSIVE + ":"
        + this.config.get(Constants.RABBITMQ_EXCHANGE_EXCLUSIVE));


    this.config.put(Constants.RABBITMQ_ROUTINGKEY,
        inputPropertyConf.get(this.type + Constants.RABBITMQ_ROUTINGKEY + this.id).toString());
    LOG.info(Constants.RABBITMQ_ROUTINGKEY + ":" + this.config.get(Constants.RABBITMQ_ROUTINGKEY));

    this.config.put(Constants.RABBITMQ_EXCHANGE_EXCLUSIVE, inputPropertyConf
        .get(this.type + Constants.RABBITMQ_EXCHANGE_EXCLUSIVE + this.id).toString());
    LOG.info(Constants.RABBITMQ_EXCHANGE_EXCLUSIVE + ":"
        + this.config.get(Constants.RABBITMQ_EXCHANGE_EXCLUSIVE));

    this.config.put(Constants.RABBITMQ_CONTENTTYPE,
        inputPropertyConf.get(this.type + Constants.RABBITMQ_CONTENTTYPE + this.id).toString());
    LOG.info(
        Constants.RABBITMQ_CONTENTTYPE + ":" + this.config.get(Constants.RABBITMQ_CONTENTTYPE));


    this.config.put(Constants.RABBITMQ_ENCODING,
        inputPropertyConf.get(this.type + Constants.RABBITMQ_ENCODING + this.id).toString());
    LOG.info(Constants.RABBITMQ_ENCODING + ":" + this.config.get(Constants.RABBITMQ_ENCODING));

    LOG.debug("Output Ending Specific,Bolt, COnfig");


  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @SuppressWarnings("rawtypes")
  public Map getConfig() {
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
