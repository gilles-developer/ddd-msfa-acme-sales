package com.acme.infra.kafka;

public interface MessageHandler {

     public void handle(String topic, int partition, long offset, String key, String value);
}
