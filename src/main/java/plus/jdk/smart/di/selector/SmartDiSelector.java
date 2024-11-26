package plus.jdk.smart.di.selector;


import plus.jdk.smart.di.global.CglibDynamicProxy;
import plus.jdk.smart.di.global.InjectBeanRegistryProcessor;
import plus.jdk.smart.di.global.SmartDependencyInjectFactory;
import plus.jdk.smart.di.properties.GlobalInjectProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationObjectSupport;

@Configuration
public class SmartDiSelector extends WebApplicationObjectSupport {

    @Bean
    InjectBeanRegistryProcessor getInjectBeanRegistryProcessor(ConfigurableBeanFactory beanFactory, GlobalInjectProperties properties,
                                                               CglibDynamicProxy cglibDynamicProxy, SmartDependencyInjectFactory smartDependencyInjectFactory) {
        return new InjectBeanRegistryProcessor(getApplicationContext(), beanFactory, properties, cglibDynamicProxy, smartDependencyInjectFactory);
    }

    @Bean
    CglibDynamicProxy getCglibDynamicProxy() {
        return new CglibDynamicProxy();
    }

    @Bean("smartDependencyInjectFactory")
    SmartDependencyInjectFactory getSmartDependencyInjectFactory() {
        return new SmartDependencyInjectFactory();
    }
}
