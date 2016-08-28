package org.lmars.dm.ua;


public class UserAuthorityInfo {

	  public String message = null;
      public boolean status = false;

      void set(boolean stat) {
          status = stat;
      }

      void set(String msg) {
          message = msg;
      }
      
}
