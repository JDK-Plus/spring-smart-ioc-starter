package plus.jdk.smart.ioc.selector;


import org.springframework.context.ApplicationContext;
import plus.jdk.smart.ioc.global.CglibDynamicProxy;
import plus.jdk.smart.ioc.global.InjectBeanRegistryProcessor;
import plus.jdk.smart.ioc.global.SmartIocSelectorFactory;
import plus.jdk.smart.ioc.properties.GlobalInjectProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationObjectSupport;

@Configuration
public class SmartIocSelector extends WebApplicationObjectSupport {

    /**
     * Gets a handler for injecting beans into the Registry.
     */
    @Bean
    InjectBeanRegistryProcessor getInjectBeanRegistryProcessor(ConfigurableBeanFactory beanFactory, GlobalInjectProperties properties,
                                                               CglibDynamicProxy cglibDynamicProxy, SmartIocSelectorFactory smartIocSelectorFactory) {
        return new InjectBeanRegistryProcessor(getApplicationContext(), beanFactory, properties, cglibDynamicProxy, smartIocSelectorFactory);
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
    SmartIocSelectorFactory getSmartDependencyInjectFactory() {
        return new SmartIocSelectorFactory(getApplicationContext());
    }
}
