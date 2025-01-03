package plus.jdk.smart.ioc.global;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibDynamicProxy implements IDynamicProxy {

    /**
     * Define the method name constant to obtain the original target object.
     */
    private static final String GET_ORIGIN_TARGET = "getOriginTarget";
    /**
     * Constant defining toString method name。
     */
    private static final String TO_STRING = "toString";

    @Override
    public <T> T acquireProxy(Class<T> targetClass, Invoker invoker) {
        Enhancer enhancer = new Enhancer();
        if (targetClass.isInterface()) {
            enhancer.setInterfaces(new Class[]{targetClass, Advised.class});
        } else {
            enhancer.setSuperclass(targetClass);
        }
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object beanInstance, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                if (TO_STRING.equals(method.getName())) {
                    return targetClass.getName() + "@" + "SDI";
                }
                if (GET_ORIGIN_TARGET.equals(method.getName())) {
                    return beanInstance;
                }
                return invoker.invoke(beanInstance, method, objects);
            }
        });
        @SuppressWarnings("unchecked")
        T proxy = (T) enhancer.create();
        return proxy;
    }
}
