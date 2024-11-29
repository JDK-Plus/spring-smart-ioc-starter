package plus.jdk.smart.di.global;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;
import org.springframework.context.ApplicationContext;
import plus.jdk.smart.di.annotations.ConditionRule;
import plus.jdk.smart.di.annotations.SmartService;
import lombok.SneakyThrows;
import plus.jdk.smart.di.model.BeanDescriptor;
import plus.jdk.smart.di.model.SmartBeanDefinition;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class SmartDependencyInjectFactory {

    /**
     * 存储 SmartService 接口与其实现类的映射关系，用于动态代理和实例化。
     */
    private final Map<Class<?>, SmartBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final ApplicationContext applicationContext;

    public SmartDependencyInjectFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 注册SDI定义到相应的智能服务组中。
     */
    protected void registerSdiDefinition(BeanDescriptor beanDescriptor) {
        beanDefinitionMap.computeIfAbsent(beanDescriptor.getSmartService().group(), k -> new SmartBeanDefinition());
        SmartBeanDefinition smartBeanDefinition = beanDefinitionMap.get(beanDescriptor.getSmartService().group());
        smartBeanDefinition.getBeanDescriptors().add(beanDescriptor);
        if(beanDescriptor.getSmartService().primary() && smartBeanDefinition.getDefaultDescriptor() != null) {
            throw new RuntimeException(String.format("interface %s primary bean %s already exists", beanDescriptor.getSmartService().group(), beanDescriptor.getBeanName()));
        }
        if(beanDescriptor.getSmartService().primary()) {
            smartBeanDefinition.setDefaultDescriptor(beanDescriptor);
        }
    }

    /**
     * 评估给定表达式并返回结果。
     * <a href="https://commons.apache.org/proper/commons-jexl/">commons-jexl</a>
     */
    public Object evalExpression(String expression, Map<String, Object> params, String beanName, String methodName) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(String.format("{%s}-{%s}", expression, params));
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext context = new MapContext();
        context.set("args", params);
        context.set("beanName", beanName);
        context.set("methodName", methodName);
        Object result = jexl.createExpression(expression).evaluate(context);
        stopWatch.stop();
        log.info("evalExpression {}, cost {}ms, {}", expression, stopWatch.getTotalTimeMillis(), stopWatch.prettyPrint());
        return result;
    }

    protected Object createObject(CglibDynamicProxy dynamicProxy, BeanDescriptor beanDescriptor) {
        Class<?> clazz = beanDescriptor.getSmartService().group();
        SmartService smartService = beanDescriptor.getSmartService();
        return dynamicProxy.acquireProxy(clazz, (proxy, method, args) -> switchBeanToInvoke(smartService.group(), method, args));
    }

    /**
     * 根据给定的 SdiDefinition 列表和方法，选择并调用相应的 bean。
     */
    @SneakyThrows
    protected Object switchBeanToInvoke(Class<?> interfaceClazz, Method method, Object[] args) {
        Map<String, Object> params =  IntStream.range(0, method.getParameterCount()).boxed()
                .collect(Collectors.toMap(i -> method.getParameters()[i].getName(), i -> args[i]));;
        SmartBeanDefinition smartBeanDefinition = beanDefinitionMap.get(interfaceClazz);
        for(BeanDescriptor beanDescriptor : smartBeanDefinition.getBeanDescriptors()) {
            Class<?> clazz = beanDescriptor.getClazz();
            Method implMethod = ClassUtil.getDeclaredMethod(clazz, method.getName(), method.getParameterTypes());
            ConditionRule conditionRule = implMethod.getAnnotation(ConditionRule.class);
            if(conditionRule == null) {
                continue;
            }
            String expression = applicationContext.getEnvironment().resolvePlaceholders(conditionRule.value());
            Object result = evalExpression(expression, params, beanDescriptor.getBeanName(), method.getName());
            log.info("eval: {}, args: {}, result:{}", conditionRule.value(), args, result);
            if(Boolean.parseBoolean(result.toString())) {
                Object object =  beanDescriptor.getBeanInstance();
                return method.invoke(object, args);
            }
        }
        throw new RuntimeException("no such implementation");
    }
}
