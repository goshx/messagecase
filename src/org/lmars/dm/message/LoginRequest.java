package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;

public class LoginRequest {

	public String userName;
	public String userPassword;
	
	public static final String UserName_Flag = "user";
	public static final String UserPassword_Flag = "password";
	
	public void fromJson(JsonObject j){
		userName = j.getString(UserName_Flag);
		userPassword = j.getString(UserPassword_Flag);
	}
	
	
	public JsonObject toJson(){
		JsonObject j = new JsonObject();
		j.put(UserName_Flag,userName);
		j.put(UserPassword_Flag,userPassword);
		return j;
	}

	public String toString(){
		return toJson().toString();
	}
}
