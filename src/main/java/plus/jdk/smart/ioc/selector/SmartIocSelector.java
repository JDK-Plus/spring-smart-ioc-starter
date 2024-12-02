package plus.jdk.smart.ioc.selector;


import org.springframework.context.ApplicationContext;
import plus.jdk.smart.ioc.global.CglibDynamicProxy;
import plus.jdk.smart.ioc.global.InjectBeanRegistryProcessor;
import plus.jdk.smart.ioc.global.GlobalSmartIocContext;
import plus.jdk.smart.ioc.global.SmartIocSelectorFactory;
import plus.jdk.smart.ioc.properties.GlobalInjectProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmartIocSelector {

    /**
     * Gets a handler for injecting beans into the Registry.
     */
    @Bean
    InjectBeanRegistryProcessor getInjectBeanRegistryProcessor(ConfigurableBeanFactory beanFactory, GlobalInjectProperties properties,
                                                               CglibDynamicProxy cglibDynamicProxy, SmartIocSelectorFactory smartIocSelectorFactory,
                                                               ApplicationContext applicationContext) {
        return new InjectBeanRegistryProcessor(applicationContext, beanFactory, properties, cglibDynamicProxy, smartIocSelectorFactory);
    }

    /**
     * Get the Cglib dynamic proxy object.
     */
    @Bean
    CglibDynamicProxy getCglibDynamicProxy() {
        return new CglibDynamicProxy();
    }

    /**
     * Get a smart dependency injection factory instance.
     */
    @Bean("smartIocSelectorFactory")
    SmartIocSelectorFactory getSmartDependencyInjectFactory(ApplicationContext applicationContext,
                                                            GlobalSmartIocContext globalSmartIocContext) {
        return new SmartIocSelectorFactory(applicationContext, globalSmartIocContext);
    }

    /**
     * Globally obtain SmartIocContext instance, used to register global variables and global functions
     */
    @Bean
    GlobalSmartIocContext getSmartIocContext() {
        return new GlobalSmartIocContext();
    }
}
