package plus.jdk.smart.ioc.global;

import java.lang.reflect.Method;

public interface Invoker {
    /**
     * Execute a method through the given proxy object and method invocation.
     */
    Object invoke(Object proxy, Method method, Object[] args);
}
