package plus.jdk.smart.ioc.global;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;
import org.springframework.context.ApplicationContext;
import plus.jdk.smart.ioc.annotations.ConditionOnRule;
import plus.jdk.smart.ioc.annotations.SmartService;
import lombok.SneakyThrows;
import plus.jdk.smart.ioc.model.BeanDescriptor;
import plus.jdk.smart.ioc.model.SmartBeanDefinition;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class SmartIocSelectorFactory {

    /**
     * Stores the mapping relationship between the SmartService interface and its implementation class for dynamic proxying and instantiation.
     */
    private final Map<Class<?>, SmartBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * Application context, used to obtain environment variables and parse expressions.
     */
    private final ApplicationContext applicationContext;

    /**
     * Create a new SmartIocSelectorFactory instance.
     */
    public SmartIocSelectorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Register the SDI definition into the corresponding smart service group.
     */
    protected void registerSdiDefinition(BeanDescriptor beanDescriptor) {
        beanDefinitionMap.computeIfAbsent(beanDescriptor.getSmartService().group(), k -> new SmartBeanDefinition());
        SmartBeanDefinition smartBeanDefinition = beanDefinitionMap.get(beanDescriptor.getSmartService().group());
        smartBeanDefinition.getBeanDescriptors().add(beanDescriptor);
        if(beanDescriptor.getSmartService().primary() && smartBeanDefinition.getDefaultDescriptor() != null) {
            throw new RuntimeException(String.format("interface %s primary bean %s already exists, Please confirm whether the configured policy is correct",
                    beanDescriptor.getSmartService().group(), beanDescriptor.getBeanName()));
        }
        if(beanDescriptor.getSmartService().primary()) {
            smartBeanDefinition.setDefaultDescriptor(beanDescriptor);
        }
    }

    /**
     * Evaluates the given expression and returns the result.
     * <a href="https://commons.apache.org/proper/commons-jexl/">commons-jexl</a>
     */
    public Object evalExpression(String expression, Map<String, Object> params, String beanName, String methodName) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(String.format("{%s}-{%s}", expression, params));
        JexlFeatures jexlFeatures = new JexlFeatures()
                // 禁止执行循环语句
                .loops(false)
                // 禁止修改全局变量
                .sideEffectGlobal(false)
                // 禁用副作用，即不允许表达式对任何变量（全局或局部）进行修改
                .sideEffect(false);;
        JexlEngine jexl = new JexlBuilder().features(jexlFeatures).create();
        JexlContext context = new MapContext();
        context.set("args", params);
        context.set("beanName", beanName);
        context.set("methodName", methodName);
        Object result = jexl.createExpression(expression).evaluate(context);
        stopWatch.stop();
        log.info("evalExpression {}, cost {}ms, {}", expression, stopWatch.getTotalTimeMillis(), stopWatch.prettyPrint());
        return result;
    }

    /**
     * Create a dynamic proxy object and bind it to the specified SmartService。
     */
    protected Object createObject(CglibDynamicProxy dynamicProxy, BeanDescriptor beanDescriptor) {
        Class<?> clazz = beanDescriptor.getSmartService().group();
        SmartService smartService = beanDescriptor.getSmartService();
        return dynamicProxy.acquireProxy(clazz, (proxy, method, args) -> switchBeanToInvoke(smartService.group(), method, args));
    }

    /**
     * Select and call the corresponding bean based on the given SdiDefinition list and method。
     */
    @SneakyThrows
    protected Object switchBeanToInvoke(Class<?> interfaceClazz, Method method, Object[] args) {
        Map<String, Object> params =  IntStream.range(0, method.getParameterCount()).boxed()
                .collect(Collectors.toMap(i -> method.getParameters()[i].getName(), i -> args[i]));;
        SmartBeanDefinition smartBeanDefinition = beanDefinitionMap.get(interfaceClazz);
        for(BeanDescriptor beanDescriptor : smartBeanDefinition.getBeanDescriptors()) {
            Class<?> clazz = beanDescriptor.getClazz();
            Method implMethod = ClassUtil.getDeclaredMethod(clazz, method.getName(), method.getParameterTypes());
            ConditionOnRule classConditionOnRule = clazz.getAnnotation(ConditionOnRule.class);
            if(classConditionOnRule != null) {
                String expression = applicationContext.getEnvironment().resolvePlaceholders(classConditionOnRule.value());
                Object result = evalExpression(expression, params, beanDescriptor.getBeanName(), method.getName());
                log.info("eval: {}, args: {}, result:{}", classConditionOnRule.value(), args, result);
                if(Boolean.parseBoolean(result.toString())) {
                    Object object =  beanDescriptor.getBeanInstance();
                    return method.invoke(object, args);
                }
            }
            ConditionOnRule methodConditionOnRule = implMethod.getAnnotation(ConditionOnRule.class);
            if(methodConditionOnRule != null) {
                String expression = applicationContext.getEnvironment().resolvePlaceholders(methodConditionOnRule.value());
                Object result = evalExpression(expression, params, beanDescriptor.getBeanName(), method.getName());
                log.info("eval: {}, args: {}, result:{}", methodConditionOnRule.value(), args, result);
                if(Boolean.parseBoolean(result.toString())) {
                    Object object =  beanDescriptor.getBeanInstance();
                    return method.invoke(object, args);
                }
            }
        }
        if(smartBeanDefinition.getDefaultDescriptor() != null) {
            return method.invoke(smartBeanDefinition.getDefaultDescriptor().getBeanInstance(), args);
        }
        throw new RuntimeException(String.format("No implementation class meeting the ConditionOnRule conditions for interface %s was found, " +
                "and no primary bean was registered, method: %s", interfaceClazz, method.getName()));
    }
}
