package com.acme.infra.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.zookeeper.common.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class ProducerService extends KafkaService {

    private KafkaProducer<String, String> kafkaProducer;

    public ProducerService(String topic) {
        super(topic);
        Properties props = setupProperties();
        kafkaProducer = new KafkaProducer<String, String>(props);
    }

    public void publish(String data, Callback callback) {

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, data);

        if(callback==null) {
            kafkaProducer.send(producerRecord);
        } else {
            kafkaProducer.send(producerRecord, callback);
        }
        kafkaProducer.flush();
    }

    public void publish(String key, String data, Callback callback) {

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, data);

        if(callback==null) {
            kafkaProducer.send(producerRecord);
        } else {
            kafkaProducer.send(producerRecord, callback);
        }
        kafkaProducer.flush();
    }

    public void publishInTransaction (ArrayList<String> dataMessages) {

        kafkaProducer.beginTransaction();

        for (String data : dataMessages) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, data);
            kafkaProducer.send(producerRecord);
        }

        kafkaProducer.commitTransaction();

        kafkaProducer.flush();
    }

    public void close() {
        kafkaProducer.close();
    }

    public static void main(String[] args) {

        String topic = "acme-travel";

        ProducerService producer = new ProducerService(topic);

        Callback callback = new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                System.out.println("Partition=" + metadata.partition() + " Offset=" + metadata.offset());
            }
        };

        for (int i = 0; i < 100; i++) {
            String key1 = "customer-" + (new Random(8)).toString();
            String key2 = "customer-" + (new Random(8)).toString();
            producer.publish(key1, "message produced: " + new Date(), callback);
            producer.publish(key2, "message produced: " + new Date(), callback);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
