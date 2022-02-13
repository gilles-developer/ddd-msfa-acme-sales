package com.acme.infra.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class ConsumerService extends KafkaService {

    private boolean stopFlag = false;

    public ConsumerService(String topic) {
        super(topic);
    }

    public ConsumerService(String topic, String groupId) {
        super(topic, groupId);
    }

    public void subscribe(Duration pollDuration, MessageHandler handler) {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(setupProperties());

        consumer.subscribe(Arrays.asList(topic));

        while (!stopFlag) {

            Set<TopicPartition> partitions = consumer.assignment();
            System.out.println("Partition assigned=" + partitions);

            ConsumerRecords<String, String> records = consumer.poll(pollDuration);
            for(ConsumerRecord<String, String> record : records) {
                handler.handle(record.topic(), record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }

    public void stop() {
        stopFlag = true;
    }


    public static void main(String[] args) {

        String topic = "acme-travel";

        String groupId = topic + "-" + (new Random(8)).toString();

        ConsumerService consumer = new ConsumerService(topic, groupId);

        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public void handle(String topic, int partition, long offset, String key, String message) {
                System.out.println("Partition=" + partition + ", offset=" + offset + ", key=" + key + ", message=" + message);
            }
        };

        long pollDuration = 1000;
        consumer.subscribe(Duration.ofSeconds(pollDuration), messageHandler);
    }
}
