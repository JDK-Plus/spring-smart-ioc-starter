package plus.jdk.smart.di.global;

import plus.jdk.smart.di.annotations.EnableSmartInject;
import plus.jdk.smart.di.properties.GlobalInjectProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSmartInject
@ConditionalOnProperty(value = "smart.inject.enable", havingValue = "true")
@EnableConfigurationProperties(GlobalInjectProperties.class)
public class SmartInjectGlobalConfiguration {

}
