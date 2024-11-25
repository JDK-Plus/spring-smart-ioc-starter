package plus.jdk.smart.di.global;

import plus.jdk.smart.di.annotations.SmartService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SmartDependencyInjectFactory {


    /**
     * 通过Cglib动态代理和SmartService实现对象的注入和管理。
     */
    public Object injectObject(CglibDynamicProxy dynamicProxy, Class<?> clazz, SmartService smartService, Map<Class<?>, List<Object>> groupImplementMap){
        Object sdiObject = dynamicProxy.acquireProxy(clazz, new Invoker() {
            @Override
            @SneakyThrows
            public Object invoke(Object proxy, Method method, Object[] args) {
                Class<?> interfaceClazz = smartService.group();
                List<Object> implementations = groupImplementMap.get(interfaceClazz);
                Object object =  implementations.get(new Random().nextInt(implementations.size()));
                Object result = method.invoke(object, args);
                return result;
            }
        });
        return sdiObject;
    }
}
