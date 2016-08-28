package org.lmars.dm.conf;


public class Config {
    ////////////////////////////////////////////////////////////////////////////
    public static final long session_alive_timespan = 100 * 1000L;
    public static final long session_check_period = 1000L;
    public static final long session_check_delay = 0;
    public static final int session_check_threadpool_size = 1;
    public static final String session_map = "session.map";
    
    
	////////////////////////////////////////////////////////////////////////////
	public static final long vertxServerNumber = 10;
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static final String key_id = "_id";
    public static final String key_key = "_key";
    public static final String key_type = "type";
    public static final String key_message = "message";
    public static final String key_offset = "offset";
    public static final String key_content = "content";
    public static final String key_contents = "contents";
    public static final String key_projects = "projects";
    public static final String key_project = "project";
    public static final String key_name = "name";
    public static final String key_hosts = "hosts";
    public static final String key_description = "description";
    public static final String key_publish_permissions = "publish_permissions";
    public static final String key_subscribe_permissions = "subscribe_permissions";
    public static final String key_publish_permission = "publish_permission";
    public static final String key_subscribe_permission = "subscribe_permission";
    public static final String key_topic = "topic";
    public static final String key_leaders = "leaders";
    public static final String key_users = "users";
    public static final String key_username = "username";
    public static final String key_user_type = "user_type";
    public static final String key_online_users = "online_users";
    public static final String key_offline_users = "offline_users";
    public static final String key_pull_topics = "pull_topics";
    public static final String key_push_topics = "push_topics";
    public static final String key_total = "total";
    public static final String key_counts = "counts";
    public static final String key_number = "number";
    public static final String key_timespan = "timespan";
    public static final String key_time_frequency= "time_frequency";
    public static final String key_count_frequency = "count_frequency";
    public static final String key_topics = "topics";
    public static final String key_subscribed_users = "subscribed_users";
    public static final String key_subscribed_topics = "subscribed_topics";
    public static final String key_ip = "ip";
    public static final String key_storage = "storage_type";
    public static final String key_time = "time";
    public static final String key_records = "time";
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static final String param_type = "type";
    public static final String param_executor_username = "executor_username";
    public static final String param_executor_password = "executor_password";
    public static final String param_username = "username";
    public static final String param_password = "password";
    public static final String param_projects = "projects";
    public static final String param_project = "project";
    public static final String param_description = "description";
    public static final String param_leaders = "leaders";
    public static final String param_users = "users";
    public static final String param_topics = "topics";
    public static final String param_topic = "topic";
    public static final String param_publish_permissions = "publish_permissions";
    public static final String param_subscribe_permissions = "subscribe_permissions";
    public static final String param_user_type = "user_type";
    public static final String param_pull_count_frequency = "pull_count_frequency";
    public static final String param_pull_time_frequency = "pull_time_frequency";
    public static final String param_push_count_frequency = "push_count_frequency";
    public static final String param_push_time_frequency = "push_time_frequency";
    public static final String param_host_group = "host_group";
    public static final String param_ips = "ips";
    public static final String param_user = "user";
    public static final String param_storages = "storages";
    public static final String param_fields = "fields";
    public static final String param_primary = "primary";
    public static final String param_indexes = "indexes";
    public static final String param_sql = "sql";
    public static final String param_lon= "lon";
    public static final String param_lat = "lat";
    public static final String param_precision = "precision";
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static final long storage_keep_alive_period = 1000L;
    public static final String storage_phoenix_jdbc_url = "jdbc:phoenix:localhost:2181";
    public static final String storage_phoenix_jdbc_username = "root";
    public static final String storage_phoenix_jdbc_password = "oseasy";
    public static final String storage_identifier = "dmessage_storage_identify";
    public static final String storage_table_user = "dmessage_user";
    public static final int storage_consumer_number = 30;
    public static final String storage_system_table_primary = "fid";
    public static final String[] storage_table_user_field = {
        param_username,param_project,param_leaders
    };
    public static final String storage_table_project = "dmessage_project";
    public static final String[] storage_table_project_field = {
        param_project,param_description
    };
    public static final String storage_table_topic = "dmessage_topics";
    public static final String[] storage_table_topic_field = {
        param_topic,param_project,param_host_group,param_storages
    };
    public static final String storage_table_host = "dmessage_host";
    public static final String[] storage_table_host_field = {
        param_ips,param_host_group
    };
    public static final String storage_table_permission = "dmessage_permission";
    public static final String[] storage_table_permission_field = {
        param_username,param_project,param_topic,param_publish_permissions,param_subscribe_permissions
    };
    public static final String storage_table_status = "dmessage_status";
    public static final String[] storage_table_status_field = {
        key_username,key_project,key_topic,key_offset,key_records
    };
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static final int manage_server_port = 7081;
    public static final int manage_server_worker_poor_size = 1;
    public static final long manage_max_event_loop_execute_time = Long.MAX_VALUE;
    public static enum manage_address 
        {createuser, deleteuser, createproject, deleteproject, createtopic, 
         deletetopic, invitetoproject, removefromproject, assignpermission, 
         getcatalog, status, disable, createhostgroup, deletehostgroup, query};
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static final int stream_server_port = 7082;
    public static final int stream_server_worker_poor_size = 100;
    public static final long stream_max_event_loop_execute_time = Long.MAX_VALUE;
    public static enum stream_address {login, send, subscribe};
    
    public static final int Message_Login = 1;
    public static final int Message_Send = 2;
    public static final int Message_Subscribe = 3;
    public static final String bootstrap_servers = "localhost:9092";
    
    public static final String driver_section = "driver";
    public static final String driver_section_type = "type";
    public static final String driver_section_h2_url = "url";

    public static final String driver_section_phoenix_url = "url";
    public static final String driver_section_phoenix_user = "user";
    public static final String driver_section_phoenix_password = "password";
  
    
//    public static enum stream_address {send, subscribe};
    public static final String stream_message_type = "type";
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static final String kafka_key_groupid = "groupid";
    public static final String kafka_key_topic = "topic";
    public static final String kafka_key_partition = "partition";
    public static final String kafka_key_offset = "offset";
    public static final String kafka_key_messagekey = "message_key";
    public static final String kafka_key_messagevalue = "message_value";
    public static final String kafka_key_partitionOffsetPair = "partition_offset";
    public static final String group_id = "group1";
    public static final String topic = "topic";
    public static final String key_serializer_class = "org.apache.kafka.common.serialization.IntegerSerializer";
    public static final String value_serializer_class = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String key_deserializer_class = "org.apache.kafka.common.serialization.IntegerDeserializer";
    public static final String value_deserializer_class = "org.apache.kafka.common.serialization.StringDeserializer";
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static enum response_type {ok, error};
    public static enum user_type {leader, user};
    public static enum assign_permission_type {basic, topic};
    public static enum get_catalog_type {project, topic, user, host};
    public static enum status_type {frequency, online};
    public static enum disable_type {auto, manual};
    
    
    ////////////////////////////////////////////////////////////////////////////
    public static final String right_query_url = "http://192.168.11.30:8082/rightsystem/right/query_right";
    public static final String user_identify_url = "http://192.168.11.30:8082/rightsystem/user/user_identify";
    public static final String user_authority_type = "type";
    public static final String user_authority_username = "username";
    public static final String user_authority_password = "password";
    public static final String user_authority_identifyinfo = "identifyinfo";
    public static final String user_authority_all = "all";
    public static final String user_authority_rightid = "rightid";
    
    ////////////////////////////////////////////////////////////////////////////
    public static final String tcp_request_type = "type";
    public static final String tcp_request_sessionid = "SessionID";
    public static final String tcp_request_project = "project";
    public static final String tcp_request_topic = "topic";
    public static final String tcp_request_content = "content";
    
    ////////////////////////////////////////////////////////////////////////////
    
    
}
