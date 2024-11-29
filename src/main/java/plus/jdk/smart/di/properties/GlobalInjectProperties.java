package plus.jdk.smart.di.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "smart.inject")
public class GlobalInjectProperties {

    /**
     * 是否启用全局注入功能。
     */
    private boolean enabled = false;
}
