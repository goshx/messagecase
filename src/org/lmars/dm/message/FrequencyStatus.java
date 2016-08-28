package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;
import org.lmars.dm.conf.Config;


public class FrequencyStatus extends OkResponse2 {
    
    public FrequencyStatus set(PullTopic[] pull_topics, PushTopic[] push_topics) {
        put(Config.key_pull_topics, pull_topics)
                .put(Config.key_push_topics, push_topics);
        return this;
    }
    
    public static class PullTopic extends JsonObject{
        
        static PullTopic obj = new PullTopic();

        public static JsonObject set(String name
                , int total
                , PullUser[] users) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_total, total)
                    .put(Config.key_users, users);
        }
        
    }
    
    public static class PushTopic extends JsonObject {
        
        static PushTopic obj = new PushTopic();

        public static JsonObject set(String name
                , int total
                , PushUser[] users) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_total, total)
                    .put(Config.key_users, users);
        }
        
    }

    private static class PullUser extends JsonObject {

        static PullUser obj = new PullUser();

        public static JsonObject set(String name
                , int counts
                , int offset
                , int timespan
                , double time_frequency
                , double count_frequency) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_counts, counts)
                    .put(Config.key_offset, offset)
                    .put(Config.key_timespan, timespan)
                    .put(Config.key_time_frequency, time_frequency)
                    .put(Config.key_count_frequency, count_frequency);
        }
        
    }
    
    private static class PushUser extends JsonObject {

        static PushUser obj = new PushUser();

        public static JsonObject set(String name
                , int counts
                , int number
                , int timespan
                , double time_frequency
                , double count_frequency) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_counts, counts)
                    .put(Config.key_number, number)
                    .put(Config.key_timespan, timespan)
                    .put(Config.key_time_frequency, time_frequency)
                    .put(Config.key_count_frequency, count_frequency);
        }
        
    }
    
}
