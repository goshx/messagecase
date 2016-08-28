package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;

public class ErrorResponse {
	
	public static final String status_Flag = "status";
	public static final String why_Flag = "why";
	
	public boolean 	status = false;
	public String 	why;
	
	public ErrorResponse(){
		
	}
	
	public ErrorResponse(String why){
		this.why = why;
	}
	
	
	public void fromJson(JsonObject j){
		why = j.getString(why_Flag);
	}
	
	public JsonObject toJson(){
		JsonObject j = new JsonObject();
		j.put(status_Flag, status);
		j.put(why_Flag, why);
		return j;
	}
	
}
