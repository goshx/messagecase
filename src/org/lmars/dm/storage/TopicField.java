package org.lmars.dm.storage;

import java.util.ArrayList;
import java.util.List;

public class TopicField {
	
	public TopicFieldType  	ftype;
	public List<String> 	msg_names = new ArrayList<String>();
	public String 			db_name;
	public boolean 			index_flag = false;
	//public boolean			is_primary_key = false;

	public void fromString(String str) throws Exception{
		
		String []s1 = str.split("\\(");
		String type_str = s1[0].trim();
		String []msg_db = s1[1].split("\\)")[0].split(":");
		String []mnames = msg_db[0].split("\\|");
		db_name = msg_db[1];
		for(String mname : mnames){
			msg_names.add(mname.trim());
		}
		
		if(type_str.equalsIgnoreCase("string")){
			ftype = TopicFieldType.t_string;
		}else if(type_str.equalsIgnoreCase("point")){
			ftype = TopicFieldType.t_point;
		}else if(type_str.equalsIgnoreCase("time")){
			ftype = TopicFieldType.t_time;
		}else if(type_str.equalsIgnoreCase("int")){
			ftype = TopicFieldType.t_int;
		}else if(type_str.equalsIgnoreCase("float")){
			ftype = TopicFieldType.t_float;
		}else if(type_str.equalsIgnoreCase("double")){
			ftype = TopicFieldType.t_double;
		}else 
			throw new Exception();
		
	}
}
