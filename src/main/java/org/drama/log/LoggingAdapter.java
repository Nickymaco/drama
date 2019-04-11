package org.drama.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LoggingAdapter<T extends LoggingInteractor> implements InvocationHandler {
    private T loggingInteractor;

    LoggingAdapter(T loggingInteractor) {
        this.loggingInteractor = loggingInteractor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	return method.invoke(this.loggingInteractor, args);
    }
    
    @SuppressWarnings("unchecked")
    public static <R extends LoggingInteractor> R adapter(R subject) {
        Class<?> clazz = subject.getClass();
        return (R) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new LoggingAdapter<>(subject));
    }
    
    public static Logging delegateLogging(LoggingFactory loggingFactory)  {
    	Logging logging = null;
    	
    	if(loggingFactory != null) {
    		logging = loggingFactory.getLogging();
    	}
    	    	
    	if(logging instanceof LoggingProxy) {
    		return logging;
    	}
    
    	return new LoggingProxy(logging);
    }
}
