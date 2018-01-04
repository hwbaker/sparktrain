package com.imooc.spark.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka消费者
 */
public class KafkaConsumer extends Thread {
    private String topic;

    public KafkaConsumer(final String topic) {
        this.topic = topic;
    }

    private ConsumerConnector createConnector() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", com.imooc.spark.kafka.KafkaProperties.ZK);
        properties.put("group.id", com.imooc.spark.kafka.KafkaProperties.GROUP_ID);

        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
    }

    @Override
    public void run() {
        ConsumerConnector consumer = createConnector();

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
//        topicCountMap.put(topic, 2);
//        topicCountMap.put(topic, 3);

        // String: topic
        // List<KafkaStream<byte[], byte[]>> 对应的数据流
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStream = consumer.createMessageStreams(topicCountMap);

        KafkaStream<byte[], byte[]> stream = messageStream.get(topic).get(0); //获取我们每次接收到的数据
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        while (iterator.hasNext()) {
            String message = new String(iterator.next().message());
            System.out.println("receive: " + message);
        }
    }
}