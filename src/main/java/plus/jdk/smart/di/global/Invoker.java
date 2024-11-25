package plus.jdk.smart.di.global;

import java.lang.reflect.Method;

public interface Invoker {
    /**
     * 通过给定的代理对象和方法调用来执行方法。
     */
    Object invoke(Object proxy, Method method, Object[] args);
}
