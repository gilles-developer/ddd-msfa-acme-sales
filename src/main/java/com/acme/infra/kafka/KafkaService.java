package com.acme.infra.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public abstract class KafkaService implements KafkaConfiguration {

    protected final String topic;

    protected final String groupId;


    public KafkaService(String topic, String groupId) {
        this.topic = topic;
        this.groupId = groupId;
    }

    public KafkaService(String topic) {
        this(topic, topic + "-consumer");

    }

    protected Properties setupProperties() {
        String serializer = StringSerializer.class.getName();
        String deserializer = StringDeserializer.class.getName();
        Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_BROKERS);
        if(groupId!=null) {
            props.put("group.id", groupId);
        }

        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        props.put("key.deserializer", deserializer);
        props.put("value.deserializer", deserializer);
        props.put("security.protocol", "PLAINTEXT");

        return props;
    }
}
