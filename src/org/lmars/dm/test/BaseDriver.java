package org.lmars.dm.test;


import java.util.Map;
import org.lmars.dm.util.TaskBuilder;


public abstract interface BaseDriver extends TaskBuilder{
    
    public enum FieldType {

        INTEGER, UNSIGNED_INT, BIGINT, UNSIGNED_LONG, TINYINT, UNSIGNED_TINYINT, SMALLINT, UNSIGNED_SMALLINT, FLOAT, UNSIGNED_FLOAT, DOUBLE, UNSIGNED_DOUBLE, DECIMAL, BOOLEAN, TIME, DATE, TIMESTAMP, UNSIGNED_TIME, UNSIGNED_DATE, UNSIGNED_TIMESTAMP, VARCHAR, CHAR, BINARY, VARBINARY, ARRAY
    };

    public class Field {

        public FieldType type;
        public String name;
        public boolean index;
        public boolean primary;
        
        public Field() {
            
        }

        public Field(FieldType type, String name, boolean primary, boolean index) {
            build(type, name, primary, index);
        }

        public Field build(FieldType type, String name, boolean primary, boolean index) {
            this.type = type;
            this.name = name;
            this.index = index;
            this.primary = primary;
            return this;
        }
    }

    public abstract boolean connect() throws Exception;
    
    public abstract boolean close() throws Exception;

    public abstract void query(String sql, Callback callback) throws Exception;
    
    public abstract void query(String name, Map<String,String> where, Callback callback) throws Exception;
    
    public abstract void query(String name, String where, Callback callback) throws Exception;

    public abstract void keepAlive();
    
    public abstract boolean update(String... sqls) throws Exception;

    public abstract boolean createTable(String name, Field... fields) throws Exception;

    public abstract boolean dropTable(String name) throws Exception;
    
    public abstract boolean clearTable(String name) throws Exception;

    public abstract boolean insertRows(String name, String[]... args) throws Exception;
    
    public abstract boolean insertRows(String name, Map<String,String>... args) throws Exception;
    
    public abstract boolean updateRows(String name, String where, String... args) throws Exception;
    
    public abstract boolean updateRows(String name, Map<String,String> where, String... args) throws Exception;
    
    public abstract boolean removeRows(String name, String where) throws Exception;
    
    public abstract boolean removeRows(String name, Map<String,String> where) throws Exception;
    
}
