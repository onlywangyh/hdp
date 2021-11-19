package com.wangyh.kafka;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @program: wangyh-common-util
 * @description: kafka util
 * @author: Wangyh
 * @create: 2021-11-19 10:54
 **/
public class KafkaUtil {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaUtil.class);
    private static final int SESSION_TIMEOUT = 30000;

    // 获取kafka的brokers
    public static String getKafkaBrokers(String kafkaClusterZk) {
        String brokers=null;
        ZooKeeper zooKeeper = null;
        try {
            String zookeeperIpStr = kafkaClusterZk;
            String kafkaStr = "";
            //第一个斜杠的位置
            int indexOfSlash = kafkaClusterZk.indexOf("/");

            if (indexOfSlash > 0) {
                zookeeperIpStr = kafkaClusterZk.substring(0, indexOfSlash);
                kafkaStr = kafkaClusterZk.substring(indexOfSlash);
            }

            String zkBrokerPath = "";
            if (!kafkaStr.endsWith("/")) {
                zkBrokerPath = kafkaStr + "/brokers/ids";
            } else {
                zkBrokerPath = kafkaStr + "brokers/ids";
            }

            LOG.info("zookeeperIpStr:" + zookeeperIpStr);
            LOG.info("kafkaStr:" + kafkaStr);
            LOG.info("zkBrokerPath:" + zkBrokerPath);

            zooKeeper = new ZooKeeper(zookeeperIpStr, SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    LOG.info("process : " + event.getType());
                }
            });
            List<String> brokerIds = zooKeeper.getChildren(zkBrokerPath, true);

            StringBuilder sb = new StringBuilder();

            for(String brokerId : brokerIds){
                String  brokerIdPath =  zkBrokerPath+"/"+brokerId;
                String content = new String( zooKeeper.getData(brokerIdPath,false,null));
                JSONObject ctnObj = new JSONObject(content);
                sb.append(ctnObj.getStr("host")).append(":").append(ctnObj.getStr("port")).append(",");
            }
            sb.deleteCharAt(sb.toString().length()-1);
            brokers = sb.toString();

        } catch (Exception e) {
            LOG.info("Exception : " + e.toString());
        } finally {
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    LOG.info("Exception : " + e.toString());
                }
            }
        }
        return brokers;
    }
}
