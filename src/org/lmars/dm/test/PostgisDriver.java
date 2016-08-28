package org.lmars.dm.test;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PostgisDriver  implements BaseDriver {
    
    private static PostgisDriver driver = new PostgisDriver();
    
    public static PostgisDriver helper() {
        return driver;
    }

    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public void query(String sql, Callback callback) {
        try {
            connect();
            String res = (new Task<String>() {
                @Override
                public String call() throws Exception {
                    Logger.getLogger(Task.class.getName())
                            .log(Level.INFO, "[Task " + sql + "]  Is Starting");
                    return Thread.currentThread().getName();
                }
            }).execute();
            Logger.getLogger(PostgisDriver.class.getName())
                    .log(Level.INFO, "[Task " + sql + "]  Is Ending");
            if(null!=callback) callback.run(res);
            close();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(PostgisDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean update(String... sqls) {
        return false;
    }

    @Override
    public void keepAlive() {
        
    }

    @Override
    public boolean dropTable(String name) {
        return false;
    }

    @Override
    public boolean clearTable(String name) {
        return false;
    }

    @Override
    public boolean insertRows(String name, String[]... args) {
        return false;
    }

    @Override
    public boolean updateRows(String name, String where, String... args) {
        return false;
    }

    @Override
    public boolean removeRows(String name, String where) {
        return false;
    }

    @Override
    public boolean createTable(String name, Field... fields) {
        return false;
    }

    @Override
    public boolean insertRows(String name, Map<String, String>... args) {
        return false;
    }

    @Override
    public void query(String name, Map<String, String> where, Callback callback) {
 
    }

    @Override
    public void query(String name, String where, Callback callback) {
        
    }

    @Override
    public boolean updateRows(String name, Map<String, String> where, String... args) {
        return false;
    }

    @Override
    public boolean removeRows(String name, Map<String, String> where) {
        return false;
    }
    
}
