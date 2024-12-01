package plus.jdk.smart.ioc.global;

public interface IDynamicProxy {

    /**
     * Get the proxy object of the specified interface.
     */
    <T> T acquireProxy(Class<T> interfaceClass, Invoker invoker);
}
