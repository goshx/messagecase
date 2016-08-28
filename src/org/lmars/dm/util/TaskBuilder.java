package org.lmars.dm.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public interface TaskBuilder {
    
    final ExecutorService exec_defalt = Executors.newWorkStealingPool();
    
    public abstract class Task<T> implements Callable<T> {
        
        @Override
        public abstract T call() throws Exception;
        
        public T execute() throws InterruptedException, ExecutionException {
            ExecutorService exec_shutdownable = Executors.newSingleThreadExecutor();
            T res = null;
            res = exec_shutdownable.submit(this).get();
            exec_shutdownable.shutdownNow();
            return res;
        }
        
        public T join() throws InterruptedException, ExecutionException {
            T res = null;
            res = exec_defalt.submit(this).get();
            return res;
        }
        
    }
    
    public abstract class Callback<T> {
        public abstract void run(T res);
    }
}
