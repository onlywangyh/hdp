package com.wangyh.kafka;


import cn.hutool.core.util.RandomUtil;
import com.wangyh.config.Config;
import org.junit.Test;

public class KafkaUtilTest {

    @Test
    public void kafkaZkTest(){
        System.out.printf(KafkaUtil.getKafkaBrokers(Config.getConfig("kafkaZk")));
    }

    @Test
    public void sendMsg2Kafka() throws InterruptedException {
        String topic = "waybillno1";
        KafkaWriter writer = new KafkaWriter();
        for (int i = 0; i < 10; i++) {
            String waybillno = RandomUtil.randomString(10);
            writer.writer(null, topic, waybillno.getBytes());
        }

        Thread.sleep(100000);

    }
}