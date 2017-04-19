package com.symantec.cpe.bolt.rabbit;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.spout.Scheme;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;

import com.symantec.cpe.config.Constants;
import com.symantec.cpe.config.TYPE;
import com.symantec.cpe.config.DO.RabbitConnectionConfigDO;
import com.symantec.cpe.spout.SchemeBuilder;
import com.symantec.cpe.trident.mapper.KafkaToRabbitMqTransactionTupleMapper;

import io.latent.storm.rabbitmq.Declarator;
import io.latent.storm.rabbitmq.config.ConfigAvailableHosts;
import io.latent.storm.rabbitmq.config.ConnectionConfig;
import io.latent.storm.rabbitmq.config.ProducerConfig;
import io.latent.storm.rabbitmq.config.ProducerConfigBuilder;
import symantec.trident.com.bolt.RabbitMqTopicSelector;
import symantec.trident.com.bolt.TridentRabbitMqStateFactory;
import symantec.trident.com.bolt.TridentRabbitMqUpdater;
import symantec.trident.com.bolt.TridentTupleToMessageNonDynamic;
import symantec.trident.com.bolt.TridentTupleToRabbitMqMapper;

public class BuildRabbitState {

	private static final Logger LOG = Logger.getLogger(BuildRabbitState.class);

	public static TridentState getState(String spoutType, Stream stream, String partitionFieldName,
			Config inputPropertyConf) {

		RabbitConnectionConfigDO rabbitMqConfig = getConfig(inputPropertyConf);

		// String outputTopic =
		// producerConfig.get(Constants.OUTPUT_TOPIC_STRING).toString();
		LOG.info("outputQueue " + rabbitMqConfig.getQueueName());

		RabbitMqTopicSelector selector = new DefaultTopicSelector(rabbitMqConfig.getQueueName());

		@SuppressWarnings("rawtypes")
		TridentTupleToRabbitMqMapper mapper = new KafkaToRabbitMqTransactionTupleMapper(partitionFieldName);

		// Declarator declarator = new CustomStormDeclarator("narendra",
		// rabbitMqConfig.getQueueName(),"dummy","direct");
		Declarator declarator = new CustomStormDeclarator(rabbitMqConfig); // think

		final String schemeType = inputPropertyConf.get(Constants.SCHEME_TYPE_STRING).toString();
		final Scheme scheme = SchemeBuilder.getScheme(schemeType);

		TridentTupleToMessageNonDynamic messageScheme = new TridentTupleToMessageNonDynamic() {

			private static final long serialVersionUID = 1L;

			@Override
			protected byte[] extractBody(TridentTuple tuple) {
				try {
					if (schemeType.toLowerCase().compareTo("string") == 0) {
						return tuple.getValueByField(scheme.getOutputFields().get(0)).toString().getBytes();
					} else {
						return (byte[]) tuple.getValueByField(scheme.getOutputFields().get(0));
					}

				} catch (Exception e) {
					LOG.error("Error while getting message from tuple \t" + "str", e);
				}
				return null;
			}

		};

		ConnectionConfig connectionConfig = new ConnectionConfig(
				ConfigAvailableHosts.fromString(rabbitMqConfig.getConfig().get(Constants.RABBIT_HA_HOST).toString()),
				rabbitMqConfig.getConfig().get(Constants.RABBIT_HOST).toString(),
				Integer.parseInt(rabbitMqConfig.getConfig().get(Constants.RABBIT_PORT).toString()),
				rabbitMqConfig.getConfig().get(Constants.RABBIT_USERNAME).toString(),
				rabbitMqConfig.getConfig().get(Constants.RABBIT_PASSWORD).toString(),
				rabbitMqConfig.getConfig().get(Constants.RABBITMQ_VIRTUALHOST).toString(), Integer.parseInt(rabbitMqConfig.getConfig().get(Constants.RABBITMQ_HEARTBEAT).toString()), false); // host,
		// port,
		// username,
		// password,
		// virtualHost,
		// heartBeat
		// ProducerConfig sinkConfig =
		// new
		// ProducerConfigBuilder().connection(connectionConfig).contentEncoding("UTF-8")
		// .contentType("application/json").exchange("exchange").routingKey(
		// rabbitMqConfig.getConfig().get(Constants.RABBIT_PASSWORD).toString()).build();

		ProducerConfig sinkConfig = new ProducerConfigBuilder().connection(connectionConfig)
				.contentEncoding(rabbitMqConfig.getConfig().get(Constants.RABBITMQ_ENCODING).toString())
				.contentType(rabbitMqConfig.getConfig().get(Constants.RABBITMQ_CONTENTTYPE).toString())
				.exchange(rabbitMqConfig.getConfig().get(Constants.RABBITMQ_EXCHANGE_NAME).toString())
				.routingKey(rabbitMqConfig.getConfig().get(Constants.RABBITMQ_ROUTINGKEY).toString()).build();

		TridentRabbitMqStateFactory stateFactory = null;

		stateFactory = new TridentRabbitMqStateFactory().withRabbitMqTopicSelector(selector)
				.withTridentTupleToRabbitMqMapper(mapper).withRabbitMqDeclarator(declarator)
				.withRabbitMqScheme(messageScheme).withSinkConfig(sinkConfig)
				.withInternalConf(rabbitMqConfig.getConfig());

		TridentState state = stream.shuffle().partitionPersist(stateFactory, new Fields(partitionFieldName),
				new TridentRabbitMqUpdater(), new Fields());

		return state;

	}

	private static RabbitConnectionConfigDO getConfig(Config inputPropertyConf) {

		// starts from zero, currently only supports writing to one

		RabbitConnectionConfigDO configDO = new RabbitConnectionConfigDO(TYPE.OUTPUT.toString(), 0, inputPropertyConf);

		LOG.info(configDO.toString());

		return configDO;

	}
}
