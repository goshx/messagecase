package org.lmars.dm.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ini4j.Profile.Section;
import org.ini4j.Wini;

public class ConfigureFileParser {
	//读取配置项
	public Map<String, String> parse(String cfgFile){
		try{
			File f = new File(cfgFile);
			Wini ini = new Wini(f);
			
			Map<String, String> all = new HashMap<String, String>();
			for(Map.Entry<String, Section> entry : ini.entrySet()){
				String prefix = entry.getKey();
				for(Map.Entry<String, String> innerEntry:entry.getValue().entrySet()){
					all.put(prefix+"."+innerEntry.getKey(), innerEntry.getValue());
				}
			}
			
			Set<String> keys = all.keySet();
			for(Map.Entry<String, String> entry : all.entrySet()){
				String val = entry.getValue();
				if(val.matches(".*\\$.*\\$.*")){
					for(String key : keys){
						if(val.contains("$"+key+"$")){
							val = val.replace("$"+key+"$", all.get(key));
							entry.setValue(val);
							break;
						}
					}
				}
			}
			
			return all;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public Map<String,Map<String,String>> parse2(String cfgFile){
		try{
			File f = new File(cfgFile);
			Wini ini = new Wini(f);
			Map<String,Map<String,String>> sections = new HashMap<String,Map<String,String>>();
			Map<String, String> all = new HashMap<String, String>();
			for(Map.Entry<String, Section> entry : ini.entrySet()){
				String prefix = entry.getKey();
				Map<String,String> values = new HashMap<String,String>();
				for(Map.Entry<String, String> innerEntry:entry.getValue().entrySet()){
					values.put(innerEntry.getKey(), innerEntry.getValue());
				}
				sections.put(prefix, values);
			}		
			return sections;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
