package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;

public class StatusResponse {

	public static final String status_Flag = "status";
	
	public boolean 	status = false;
		
	public StatusResponse fromJson(JsonObject j){
		status = j.getBoolean(status_Flag);
		return this;
	}
	
}
