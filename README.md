# Data Pipe

## Goal : Reading streaming data from rabbbitmq or kafka and writing the data to another endpoint and add Custom Logic if needed

1. Property File : src/main/resources/*.Properties
2. Custom Logic  : File com.symantec.cpe.storm.LogicBolt -> Add any custom logic in execute method, We will add different spouts down the line 


## Building project 

1. Download this project(rabbitmq spout and ) https://github.com/ppat/storm-rabbitmq  and install the jar
2. mvn install:install-file -Dfile=${WORKSPACE}/${PROJECT}/storm-rabbitmq-0.6.2-SNAPSHOT.jar -DgroupId=io.latent -DartifactId=storm-rabbitmq  -Dversion=0.6.2-SNAPSHOT -Dpackaging=jar
3. mvn clean test package -U

## Deploying project 

1. Update the property File
2. Add any logic if required in the Logic Bolt , build Jar as per above
3. Run as per the below command

## Run Options 
Local mode within eclipse IDE or Remote to any cluster

```sh
eg : storm jar  data-pipe-storm-0.0.2-SNAPSHOT-jar-with-dependencies.jar -c nimbus.host=<hostname> -c nimbus.port=<port_number> com.symantec.cpe.StartService <PropertyFile>
eg : storm jar  data-pipe-storm-0.0.2-SNAPSHOT-jar-with-dependencies.jar com.symantec.cpe.StartService <PropertyFile>
```


## Kafka Input Parameters
```sh
Property          	Type      (format)Example
streamName         String     datapipe
sourceZooKeeperURL String     localhost:2181
inputTopic         String     source-ga 
 ```

## RabbitMQ Input Parameters
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

Note : Only one source point at a time, either Kafka or RabbitMq
