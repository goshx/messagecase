package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;

public class SendMessageResponse {
	public static final String status_Flag = "status";
	
	public boolean 	status = false;
	
	public void fromJson(JsonObject j){
		status = j.getBoolean(status_Flag);
	}
	
}
