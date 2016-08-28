package org.lmars.dm.storage.drivers;

import io.vertx.core.json.JsonObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lmars.dm.conf.Config;
import org.lmars.dm.main.agent.StreamMessage;
import org.lmars.dm.storage.IStorgeDriver;
import org.lmars.dm.storage.TopicField;
import org.lmars.dm.storage.TopicMapping;

public class PhoenixDriver implements IStorgeDriver{

	private Connection conn = null;
	private Map<String, TopicMapping> topics_info = null;
	private String jdbc_url = null;
	private String jdbc_user = null;
	private String jdbc_password = null;
	
    public boolean reConnect() {
        boolean res = false;
        try {
        	if(conn != null)
        		conn.close();
            conn = DriverManager.getConnection(jdbc_url, jdbc_user,jdbc_password);
            res = true;
        } catch (SQLException ex) {
            Logger.getLogger(PhoenixDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
	public void init(Map<String, String> conf,Map<String, TopicMapping> topics) throws Exception{
		
		topics_info = topics;
		
		jdbc_url = conf.get(Config.driver_section_phoenix_url);
		jdbc_user = conf.get(Config.driver_section_phoenix_user);
		jdbc_password = conf.get(Config.driver_section_phoenix_password);
		
		reConnect();
		
		/*try{
			conn.createStatement().execute("create table if not exists t2(v varchar primary key)");
			Statement stat = conn.createStatement();	
			stat.execute("upsert into t2(v) values('ddd')");
			conn.commit();
			
			ResultSet rs = conn.createStatement().executeQuery("select * from t2");
			while(rs.next()){
				String c= rs.getString(1);
				int err;
				err = 3;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}*/

		/*
		try{		
		ResultSet rs = conn.createStatement().executeQuery("select count(*) from test_table1");
		while(rs.next()){
			Long c= rs.getLong(1);
			int err;
			err = 3;
		}			
	}catch(Exception e){
		e.printStackTrace();
	}*/
		
	    
        for(TopicMapping topic : topics.values()){
        	
        	StringBuilder sb = new StringBuilder();
        	sb.append("create table if not exists ").append(topic.table).append("(");
        	
        	List<TopicField> fs = new ArrayList<TopicField>();
        	fs.addAll(topic.fieldMapping);
        	for(int i = 0;i<fs.size();i++){
        		TopicField f = fs.get(i);
        		switch(f.ftype){
        		case t_string:
        			sb.append(f.db_name).append(" VARCHAR not null ");
        			break;
        		case t_point:
        			sb.append(f.db_name).append("_x").append(" FLOAT ,");
        			sb.append(f.db_name).append("_y").append(" FLOAT ");
        			break;
        		case t_time:
        			sb.append(f.db_name).append(" BIGINT not null ");
        			break;
        		case t_int:
        			sb.append(f.db_name).append(" INTEGER ");
        			break;
        		case t_float:
        			sb.append(f.db_name).append(" FLOAT ");
        			break;
        		case t_double:
        			sb.append(f.db_name).append(" DOUBLE ");
        			break;
        		}
        		
        		/*if(f.is_primary_key)
        			sb.append(" primary key ");*/        		
        		sb.append(",");
        	}
        	
        	sb.append("CONSTRAINT pk PRIMARY KEY (").append(topic.pkey).append(")");
        	sb.append(")");
        	
        	try(Statement stat = conn.createStatement();){
        		String sql = sb.toString();
        		stat.execute(sql);        		
        	}catch(Exception e){
        		e.printStackTrace();
        	}

        	for(int i = 0;i<fs.size();i++){
        		TopicField f = fs.get(i);
        		if(f.index_flag){
        			
        			switch(f.ftype){
            		case t_string:
            		case t_time:
            		case t_int:
            		case t_float:
            		case t_double:
            			sb = new StringBuilder();
                    	sb.append("create index if not exists ").append(f.db_name).append("_index ON ")
                    		.append(topic.table).append("(").append(f.db_name).append(")"); 
                    	try(Statement stat = conn.createStatement();){
                    		String sql = sb.toString();
                    		stat.execute(sql);        		
                    	}catch(Exception e){
                    		
                    	}
            			break;
            		case t_point:
            			sb = new StringBuilder();
                    	sb.append("create index if not exists ").append(f.db_name).append("_x_index ON ")
                    		.append(topic.table).append("(").append(f.db_name).append("_x)"); 
                    	String sql1 = sb.toString();
            			sb = new StringBuilder();
                    	sb.append("create index if not exists ").append(f.db_name).append("_y_index ON ")
                    		.append(topic.table).append("(").append(f.db_name).append("_y)"); 
                    	String sql2 = sb.toString();
                    	try(Statement stat1 = conn.createStatement();Statement stat2 = conn.createStatement();){
                    		stat1.execute(sql1);        		
                    		stat2.execute(sql2);        		
                    	}catch(Exception e){
                    		
                    	}
                    	break;            		
            		}
        		}
        	}
        	
        	
        }
        
	}
	
	public void saveStreamData(List<StreamMessage> msgs) throws Exception{
		
		try(Statement stat = conn.createStatement();){
			
			for(StreamMessage msg : msgs){
				try{
					TopicMapping topic_map = this.topics_info.get(msg.topic);
					if(topic_map == null)
						continue;
					JsonObject j_obj = new JsonObject(msg.content);
					StringBuilder sb_keys = new StringBuilder();
					StringBuilder sb_vals = new StringBuilder();
					sb_keys.append("upsert into ").append(topic_map.table).append(" (");
					sb_vals.append("values(");
					int fsize = topic_map.fieldMapping.size();
					int fiter = 0;
					boolean empty_flag= true;
					for(TopicField f : topic_map.fieldMapping){
						fiter++;
						boolean flag = false;
						switch(f.ftype){
		            		case t_string:{
		            			String v = j_obj.getString(f.msg_names.get(0));
		            			if(v != null){
			            			sb_keys.append(f.db_name);
			            			sb_vals.append("'").append(v).append("'");
			            			flag = true;
			            			empty_flag = false;
		            			}	            			
		            			break;
		            		}
		            		case t_time:{
		            			Long v = j_obj.getLong(f.msg_names.get(0));
		            			if(v != null){
			            			sb_keys.append(f.db_name);
			            			sb_vals.append(v);
			            			flag = true;
			            			empty_flag = false;
		            			}
		            			break;	            			
		            		}
		            		case t_int:{
		            			Integer v = j_obj.getInteger(f.msg_names.get(0));
		            			if(v != null){
			            			sb_keys.append(f.db_name);
			            			sb_vals.append(v);
			            			flag = true;
			            			empty_flag = false;
		            			}
		            			break;	            			
		            		}
		            		case t_float:{
		            			Float v = j_obj.getFloat(f.msg_names.get(0));
		            			if(v != null){
			            			sb_keys.append(f.db_name);
			            			sb_vals.append(v);
			            			flag = true;
			            			empty_flag = false;
		            			}
		            			break;	            			
		            		}
		            		case t_double:{
		            			Double v = j_obj.getDouble(f.msg_names.get(0));
		            			if(v != null){
			            			sb_keys.append(f.db_name);
			            			sb_vals.append(v);
			            			flag = true;
			            			empty_flag = false;
		            			}
		            			break;	            			
		            		}
		            		case t_point:{
								Float x = j_obj.getFloat(f.msg_names.get(0));
								Float y =j_obj.getFloat(f.msg_names.get(1));
								if(x != null && y != null){
			            			sb_keys.append(f.db_name).append("_x,").append(f.db_name).append("_y");
									sb_vals.append(x).append(",").append(y);	
									flag = true;
			            			empty_flag = false;
								}
		            			break;	            			
		            		}
						}
						
						if(fiter == fsize){
							flag = false;
						}
						if(flag){
							sb_keys.append(",");
							sb_vals.append(",");
						}
					}
					
					sb_keys.append(")");
					sb_vals.append(")");
					
					if(!empty_flag){
						sb_keys.append(" ").append(sb_vals.toString());
						String sql = sb_keys.toString();
						stat.addBatch(sql);
					}
					
				}catch(Exception ignore){
					ignore.printStackTrace();
				}
			}
			
			try{
				stat.executeBatch();
				conn.commit();				
			}catch(Exception e){
				try{
					conn.rollback();	
				}catch(Exception ignore){
					throw e;
				}
			}
			
    	}catch(Exception e){
    		//出现异常，休息一下重新连接
    		Thread.sleep(3000);
    		reConnect();
    		e.printStackTrace();
    		throw e;
    	}
		
	}
	
	public void close(){
		
	}
	
	
}
