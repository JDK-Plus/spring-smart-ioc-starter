package plus.jdk.smart.di.model;

import lombok.Builder;
import lombok.Data;
import plus.jdk.smart.di.annotations.SmartService;

@Data
@Builder
public class SdiDefinition {

    private String beanName;

    private Class<?> clazz;

    private SmartService smartService;

    private Object beanInstance;
}
