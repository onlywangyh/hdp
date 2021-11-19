package com.wangyh.kafka;

import com.wangyh.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * @program: wangyh-common-util
 * @description:
 * @author: Wangyh
 * @create: 2021-11-19 10:37
 **/
@Slf4j
public class KafkaWriter {
    // create instance for properties to access producer configs
    Properties props;

    Producer<String, byte[]> producer;

    KafkaWriter() {
        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", KafkaUtil.getKafkaBrokers(Config.getConfig("kafkaZk")));

        //Set acknowledgements for producer requests.
        props.put("acks", "1");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 3);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        this.producer = new KafkaProducer<String, byte[]>(props);
    }

    public void writer(String key, String topic, byte[] value) {
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(topic, key, value);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e == null) {
                    log.info("send msg to kafka topic:{} success", recordMetadata.topic());
                } else {
                    log.info("send msg to kafka err ", e);
                }
            }
        });
    }
}
