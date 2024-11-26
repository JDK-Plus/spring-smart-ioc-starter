package plus.jdk.smart.di.global;

import plus.jdk.smart.di.annotations.SmartService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import plus.jdk.smart.di.model.SdiDefinition;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SmartDependencyInjectFactory {

    private final Map<Class<?>, List<SdiDefinition>> groupImplementMap = new ConcurrentHashMap<>();

    protected void registerSdiDefinition(SdiDefinition sdiDefinition) {
        groupImplementMap.computeIfAbsent(sdiDefinition.getSmartService().group(), k -> new ArrayList<>()).add(sdiDefinition);
    }

    protected Object injectObject(CglibDynamicProxy dynamicProxy, SdiDefinition sdiDefinition) {
        Class<?> clazz = sdiDefinition.getSmartService().group();
        SmartService smartService = sdiDefinition.getSmartService();
        Object sdiObject = dynamicProxy.acquireProxy(clazz, new Invoker() {
            @Override
            @SneakyThrows
            public Object invoke(Object proxy, Method method, Object[] args) {
                Class<?> interfaceClazz = smartService.group();
                List<SdiDefinition> implementations = groupImplementMap.get(interfaceClazz);
                Object object =  implementations.get(new Random().nextInt(implementations.size())).getBeanInstance();
                return method.invoke(object, args);
            }
        });

        return sdiObject;
    }
}
