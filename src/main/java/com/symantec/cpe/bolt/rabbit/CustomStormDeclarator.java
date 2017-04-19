package com.symantec.cpe.bolt.rabbit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.symantec.cpe.config.Constants;
import com.symantec.cpe.config.DO.RabbitConnectionConfigDO;

import io.latent.storm.rabbitmq.Declarator;

//import io.latent.storm.rabbitmq.Declarator;

public class CustomStormDeclarator 
 
  implements Declarator {


  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String exchange = "";
  private String queue = "Rabbit";
  private boolean queueDurable = true;
  private String routingKey = "";
  private String exchangeType = "topic";
  private boolean exchangeDurable = true;

  private boolean exchangeExclusive = false;

//   public CustomStormDeclarator(String exchange, String queue) {
//   this(exchange, queue, "");
//   }
//  
//   public CustomStormDeclarator(String exchange, String queue, String routingKey) {
//   this(exchange,queue,routingKey,"");
//   }
//  
//   public CustomStormDeclarator(String exchange, String queue, String routingKey,String
//   exchangeType) {
//   this.exchange = exchange;
//   this.queue = queue;
//   this.routingKey = routingKey;
//   this.exchangeType =exchangeType;
//   }

  public CustomStormDeclarator(RabbitConnectionConfigDO rabbitConnectionConfigDO) {

    this.exchange =
        rabbitConnectionConfigDO.getConfig().get(Constants.RABBITMQ_EXCHANGE_NAME).toString();
    this.queue = rabbitConnectionConfigDO.getQueueName();
    if("TRUE".compareToIgnoreCase(rabbitConnectionConfigDO.getConfig().get(Constants.RABBIT_QUEUE_DURABLE).toString())==0){
    	this.queueDurable = true;
    }else{
    	this.queueDurable = false;
    }
    
     
    this.routingKey =
        rabbitConnectionConfigDO.getConfig().get(Constants.RABBITMQ_ROUTINGKEY).toString();
    this.exchangeType =
        rabbitConnectionConfigDO.getConfig().get(Constants.RABBITMQ_EXCHANGE_TYPE).toString();
    
    if("TRUE".compareToIgnoreCase(rabbitConnectionConfigDO.getConfig().get(Constants.RABBITMQ_EXCHANGE_DURABLE).toString())==0){
    	this.exchangeDurable = true;
    }else{
    	this.exchangeDurable = false;
    }
    
    if("TRUE".compareToIgnoreCase(rabbitConnectionConfigDO.getConfig().get(Constants.RABBITMQ_EXCHANGE_EXCLUSIVE).toString())==0){
    	this.exchangeExclusive = true;
    }else{
    	this.exchangeExclusive = false;
    }
    
     
  }


  @Override
  public void execute(com.rabbitmq.client.Channel channel) {
    // you're given a RabbitMQ Channel so you're free to wire up your exchange/queue bindings as you
    // see fit
    try {
      Map<String, Object> args = new HashMap<>();
      channel.queueDeclare(queue, queueDurable,exchangeExclusive , false, args);
      channel.exchangeDeclare(exchange, exchangeType, exchangeDurable);
      channel.queueBind(queue, exchange, routingKey);
//      channel.queueDeclare("demo5", false, false, false, args);
//      channel.exchangeDeclare("asdf", "topic", true);
//      channel.queueBind("demo5", "asd", "asd");
    } catch (IOException e) {
      throw new RuntimeException("Error executing rabbitmq declarations.", e);
    }
  }
}
