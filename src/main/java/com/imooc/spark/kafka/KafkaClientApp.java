package com.imooc.spark.kafka;

/**
 * Kafka java 客户端测试
 */
public class KafkaClientApp {

    public static void main(String[] args) {

        new KafkaProducer(KafkaProperties.TOPIC).start();

        new KafkaConsumer(KafkaProperties.TOPIC).start();

    }
}
