package plus.jdk.smart.ioc.global;

import plus.jdk.smart.ioc.annotations.EnableSmartInject;
import plus.jdk.smart.ioc.properties.GlobalInjectProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSmartInject
@ConditionalOnProperty(value = "smart.ioc.enable", havingValue = "true")
@EnableConfigurationProperties(GlobalInjectProperties.class)
public class SmartIocGlobalConfiguration {

}
