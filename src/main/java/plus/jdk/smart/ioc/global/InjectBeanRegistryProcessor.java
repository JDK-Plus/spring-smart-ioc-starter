package plus.jdk.smart.ioc.global;

import plus.jdk.smart.ioc.annotations.SmartService;
import plus.jdk.smart.ioc.model.BeanDescriptor;
import plus.jdk.smart.ioc.properties.GlobalInjectProperties;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.type.MethodMetadata;

import java.util.Objects;

public class InjectBeanRegistryProcessor implements BeanDefinitionRegistryPostProcessor, Ordered {

    /**
     * 应用程序上下文，用于获取bean实例。
     */
    private final ApplicationContext applicationContext;

    /**
     * Bean工厂，用于管理和注册bean实例。
     */
    private final ConfigurableBeanFactory beanFactory;

    /**
     * 全局注入属性配置，用于存储和管理注入相关的配置信息。
     */
    private final GlobalInjectProperties properties;

    /**
     * CGLib动态代理对象，用于生成和管理动态代理实例。
     */
    private final CglibDynamicProxy cglibDynamicProxy;

    /**
     * 注入工厂对象，用于创建注入实例。
     */
    private final SmartIocSelectorFactory smartIocSelectorFactory;

    /**
     * 构造函数，初始化注入bean的注册处理器。
     *
     * @param applicationContext 应用上下文对象，用于获取bean实例。
     * @param beanFactory        可配置的bean工厂，用于注册bean。
     * @param properties         全局注入属性对象，用于获取注入配置信息。
     */
    public InjectBeanRegistryProcessor(ApplicationContext applicationContext, ConfigurableBeanFactory beanFactory, GlobalInjectProperties properties,
                                       CglibDynamicProxy cglibDynamicProxy, SmartIocSelectorFactory smartIocSelectorFactory) {
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
        this.properties = properties;
        this.cglibDynamicProxy = cglibDynamicProxy;
        this.smartIocSelectorFactory = smartIocSelectorFactory;
    }

    protected String resolveBeanClassName(BeanDefinition beanDefinition) {
        if (StringUtils.isNotEmpty(beanDefinition.getBeanClassName())) {
            return beanDefinition.getBeanClassName();
        }
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            MethodMetadata factoryMethodMetadata = ((AnnotatedBeanDefinition) beanDefinition).getFactoryMethodMetadata();
            if (factoryMethodMetadata != null && StringUtils.isNotEmpty(factoryMethodMetadata.getReturnTypeName())) {
                return factoryMethodMetadata.getReturnTypeName();
            }
            //应该不用判断 ((AnnotatedBeanDefinition) beanDefinition).getMetadata()了
        }
        return null;
    }

    @SneakyThrows
    protected static Class<?> tryLoadClass(ClassLoader classLoader, String interfaceClassName) {
        return classLoader.loadClass(interfaceClassName);
    }

    protected Class<?> loadClass(String className, BeanDefinitionRegistry registry) throws RuntimeException {
        ConfigurableBeanFactory cbf = (ConfigurableBeanFactory) registry;
        Class<?> clazz = tryLoadClass(Objects.requireNonNull(cbf.getBeanClassLoader()), className);
        if (clazz != null) {
            return clazz;
        }
        if (cbf.getTempClassLoader() == null) {
            throw new RuntimeException("Please check whether the class exists in the module's dependencies:" + className);
        }
        return tryLoadClass(cbf.getTempClassLoader(), className);
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String beanDefinitionName : applicationContext.getBeanNamesForAnnotation(SmartService.class)) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            Object beanObj = applicationContext.getBean(beanDefinitionName);
            String beanClassName = resolveBeanClassName(beanDefinition);
            Class<?> beanClass = loadClass(beanClassName, registry);
            SmartService smartService = beanClass.getAnnotation(SmartService.class);
            BeanDescriptor beanDescriptor = BeanDescriptor.builder()
                    .beanName(beanDefinitionName)
                    .clazz(beanClass)
                    .smartService(smartService)
                    .beanInstance(beanObj)
                    .build();
            RootBeanDefinition groupBeanDefinition = (RootBeanDefinition) BeanDefinitionBuilder.rootBeanDefinition(SmartIocSelectorFactory.class)
                    .setFactoryMethodOnBean("createObject", "smartIocSelectorFactory")
                    .addConstructorArgValue(cglibDynamicProxy)
                    .addConstructorArgValue(beanDescriptor)
                    .setLazyInit(false)
                    .setScope(BeanDefinition.SCOPE_SINGLETON)
                    .getBeanDefinition();
            groupBeanDefinition.setTargetType(smartService.group());
            smartIocSelectorFactory.registerSdiDefinition(beanDescriptor);
            Class<?> groupClazz = smartService.group();
            String interfaceBeanName = Character.toLowerCase(groupClazz.getSimpleName().charAt(0)) + groupClazz.getSimpleName().substring(1);
            if (!registry.containsBeanDefinition(interfaceBeanName)) {
                registry.registerBeanDefinition(interfaceBeanName, groupBeanDefinition);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
