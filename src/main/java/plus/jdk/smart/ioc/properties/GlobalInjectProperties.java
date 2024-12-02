package plus.jdk.smart.ioc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "smart.ioc")
public class GlobalInjectProperties {

    /**
     * Whether to enable global injection function.
     */
    private boolean enable = false;
}
