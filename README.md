# Identity Topology

## Goal : Reading streaming data from rabbbitmq or kafka and writing the data to another endpoint and add Custom Logic if needed

1. Property File : src/main/resources/*.Properties
2. We are trying to achieve Exactly once using Trident, at this time it is close but not there yet.


## Building project 

1. Download this project(rabbitmq spout and ) https://github.com/ppat/storm-rabbitmq  and install the jar <WE will submit the pull request for the TridentSpout which is missing>
2. mvn install:install-file -Dfile=${WORKSPACE}/${PROJECT}/storm-rabbitmq-0.6.2-SNAPSHOT.jar -DgroupId=io.latent -DartifactId=storm-rabbitmq  -Dversion=0.6.2-SNAPSHOT -Dpackaging=jar
3. mvn clean test package -U

## Deploying project 

1. Update the property File
2. Run as per the below command

## Run Options 
Local mode within eclipse IDE or Remote to any cluster

```sh
eg : storm jar   -c nimbus.host=<hostname> -c nimbus.port=<port_number> IdentityTopology-1.0.0-jar-with-dependencies.jar com.symantec.cpe.StartService <PropertyFile>
eg : storm jar  IdentityTopology-1.0.0-jar-with-dependencies.jar com.symantec.cpe.StartService <PropertyFile>
```


## Kafka Input Parameters
```sh
Property          	Type      (format)Example
streamName         String     datapipe
sourceZooKeeperURL String     localhost:2181
inputTopic         String     source-ga 
 ```

## RabbitMQ Input Parameters, Can Read from Two EndPoints of Rabbit
```sh
Property          	Type      (format)Example
rabbitmq.host    	String	localhost
rabbitmq.port		Integer	5672 (default)
rabbitmq.username	String	admin
rabbitmq.password	String	admin
rabbitmq.prefetchCount	Integer	200
rabbitmq.queueName	String	hello
rabbitmq.requeueOnFail	Boolean	true
 ```

## Kafka Output Parameters
```sh
Property					Type (format)	Example											Comment
destinationKafkaURL			String			localhost\:6667   		Comma-separated list of all Kafka brokers at the destination cluster.
outputTopic					String			Kafka_Replication
destinationZooKeeperURL		String			localhost\:2181			Comma-separated list of all ZooKeeper URLs in the destination cluster.
```

## Topology Parameters
```sh			
Property					Type (format)	Example								Comment
serializerEncodingValue		String			kafka.serializer.DefaultEncoder		For bytes, use the default kafka.serializer.DefaultEncoder. For string, use kafka.serializer.StringEncoder
partitionFieldName			String			bytes								Use bytes for bytes, and str for StringScheme.
schemeType					String			raw	Use raw for RawScheme (bytes), and string for StringScheme.
topologyName				String			datapipe-kafka
streamName					String			datapipe
requiredAcks				Integer			-1	
parallelCount				Integer			1	 
metricsParallelCount		Integer			1	 
topology.workers			Integer			1										Maximum value for this parameter is equal to the number of Storm supervisor nodes on the cluster.
```

## Addtional Refrence / Good Read
1. https://cwiki.apache.org/confluence/display/KAFKA/Idempotent+Producer
2. 

## Note : 
Only one source point at a time, either Kafka or RabbitMq
