package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;

public class OKResponse {

	public static final String status_Flag = "status";
	
	public boolean 	status = true;
		
	public JsonObject toJson(){
		JsonObject j = new JsonObject();
		j.put(status_Flag, status);
		return j;
	}
	
}
