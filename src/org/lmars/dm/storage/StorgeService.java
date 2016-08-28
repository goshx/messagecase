package org.lmars.dm.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.lmars.dm.conf.Config;
import org.lmars.dm.main.agent.StreamMessage;
import org.lmars.dm.util.BaseServer;
import org.lmars.dm.util.ConfigureFileParser;

public class StorgeService extends BaseServer {

    public static String cfgFile = null;
    private IStorgeDriver driver = null;
    private ExecutorService es = null;
    private KafkaConsumer<Integer, String> consumer = null;
    private Map<String, TopicMapping> topics = new HashMap<String, TopicMapping>();
    private String kafka_service = null;
    private String groupID = null;

    public static void main(String args[]) {
        if(args.length>0) {
            cfgFile = args[0];
        } else {
            cfgFile = "C:\\Users\\Junia\\Documents\\NetBeansProjects\\DMessage\\conf\\storge.cfg";
        }
        BaseServer.create(StorgeService.class, Config.stream_server_worker_poor_size, Config.stream_max_event_loop_execute_time);

    }

    public void readConfig() throws Exception {
        //加载配置信息
        ConfigureFileParser cfg = new ConfigureFileParser();
        Map<String, Map<String, String>> sections = cfg.parse2(cfgFile);
        for (Map.Entry<String, Map<String, String>> section : sections.entrySet()) {
            String sectionName = section.getKey();
            String prefix = "Topic";
            if (sectionName.length() > prefix.length()) {
                String[] ss = sectionName.trim().split(":");
                if (ss.length == 2) {
                    if (ss[0].equalsIgnoreCase(prefix)) {
                        Map<String, String> info = section.getValue();
                        TopicMapping tmapping = new TopicMapping();
                        tmapping.topic = ss[1];
                        tmapping.table = info.get("table");
                        String indexs = info.get("indexs");
                        tmapping.pkey = info.get("primary_key");
                        String fields = info.get("fields");
                        String[] index_s = indexs.split(",");
                        Set<String> which_idx = new HashSet<String>();
                        for (String idx : index_s) {
                            which_idx.add(idx);
                        }
                        String[] fs = fields.split(",");
                        for (String f : fs) {
                            TopicField tf = new TopicField();
                            tf.fromString(f);
                            if (which_idx.contains(tf.db_name)) {
                                tf.index_flag = true;
                            }
                            tmapping.fieldMapping.add(tf);
                        }

                        topics.put(tmapping.topic, tmapping);

                    }
                }
            }
        }

        //
        Map<String, String> driver_conf = sections.get("driver");
        driver = DriverFactory.create(driver_conf, topics);

        kafka_service = sections.get("kafka").get("servers");
        groupID = sections.get("kafka").get("groupid");

    }

    private void startKafka(String kafkaServers) {

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServers);

        props.put("group.id", groupID);
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<Integer, String>(props);
        List<String> topics_list = new ArrayList<String>();
        topics_list.addAll(topics.keySet());
        consumer.subscribe(topics_list);

        try {
            String content = "{\"device\":\"pool-64-thread-1\",\"time\":1462857456716,\"lat\":31.56896527849839,\"lon\":115.53437195945247,\"precision\":35}";
            List<StreamMessage> buffer2 = new ArrayList<StreamMessage>();
            buffer2.add(new StreamMessage("dmessage", content));
            driver.saveStreamData(buffer2);
        } catch (Exception e) {

        }

        es = Executors.newFixedThreadPool(1);
        es.submit(() -> {

            final int minBatchSize = 200;
            List<StreamMessage> buffer = new ArrayList<StreamMessage>();
            while (true) {
                try {
                    ConsumerRecords<Integer, String> records = consumer.poll(1000);
                    for (ConsumerRecord<Integer, String> record : records) {
                        buffer.add(new StreamMessage(record.topic(), record.value()));
                    }
                    if (buffer.size() >= minBatchSize) {
                        driver.saveStreamData(buffer);
                        consumer.commitSync();
                        buffer.clear();
                    }
                } catch (Exception e) {

                }
            }

        });
    }

    @Override
    public void start() throws Exception {

        readConfig();

        startKafka(kafka_service);

    }

}
