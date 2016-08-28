package org.lmars.dm.storage;

import java.util.Map;

import org.lmars.dm.conf.Config;
import org.lmars.dm.storage.drivers.H2Driver;
import org.lmars.dm.storage.drivers.PhoenixDriver;


public class DriverFactory {
    
    public enum Type {h2,postgis, phoenix, none}
    
    public static IStorgeDriver create(Map<String,String> conf,Map<String, TopicMapping> topics) throws Exception{
    	
    	String type = conf.get(Config.driver_section_type);
    	IStorgeDriver driver = null;
    	if(type.equalsIgnoreCase("h2")){
    		driver = new H2Driver();
    	}else if(type.equalsIgnoreCase("phoenix")){
    		driver = new PhoenixDriver();
    	}
    	
    	driver.init(conf,topics);
    	return driver;
    	
    }
    
}
