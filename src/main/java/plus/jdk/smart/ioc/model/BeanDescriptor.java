package plus.jdk.smart.ioc.model;

import lombok.Builder;
import lombok.Data;
import plus.jdk.smart.ioc.annotations.SmartService;

/**
 * Bean descriptor class, used to encapsulate Bean related information
 * This includes the name of the Bean, the corresponding class type, the smart service interface provided, and the Bean instance.
 */
@Data
@Builder
public class BeanDescriptor {

    /**
     * The name of the bean, used to uniquely identify a bean
     */
    private String beanName;

    /**
     * The class type corresponding to the Bean, which determines the specific type when the Bean is instantiated.
     */
    private Class<?> clazz;

    /**
     * The intelligent service interface provided is used to implement advanced service logic, such as dynamic configuration, health check, etc.
     */
    private SmartService smartService;

    /**
     * Bean instance object, used to store the actual runtime instance of the Bean
     */
    private Object beanInstance;
}
