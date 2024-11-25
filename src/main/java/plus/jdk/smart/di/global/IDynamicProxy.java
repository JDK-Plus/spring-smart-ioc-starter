package plus.jdk.smart.di.global;

public interface IDynamicProxy {

    <T> T acquireProxy(Class<T> interfaceClass, Invoker invoker);
}
