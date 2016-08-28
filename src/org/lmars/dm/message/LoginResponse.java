package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;
import org.lmars.dm.conf.Config;

public class LoginResponse{

	public static final String sessionID_Flag = "sid";
	public static final String status_Flag = "status";
	
	public boolean 	status = false;
	public String 	sessionID;
	
	public LoginResponse(){
		
	}
	
	public LoginResponse(boolean status, String sid){
		this.status = status;
		this.sessionID = sid;
	}
	
	
	public void fromJson(JsonObject j){
		status = j.getBoolean(status_Flag);
		sessionID = j.getString(sessionID_Flag);
	}
	
	public JsonObject toJson(){
		JsonObject j = new JsonObject();
		j.put(status_Flag, status);
		if(sessionID != null)
			j.put(sessionID_Flag, sessionID);
		return j;
	}
	
	
}
