package plus.jdk.smart.ioc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The SmartBeanDefinition class is used to manage a set of Bean descriptors and a default Bean descriptor.
 * It is mainly used in scenarios where Bean instantiation configuration needs to be dynamically determined.
 */
@Data
public class SmartIocDefinition {

    /**
     * Stores a set of Bean descriptors used to describe different Bean instantiation configurations
     */
    private List<BeanDescriptor> beanDescriptors = new ArrayList<>();

    /**
     * The default bean descriptor that will be used to instantiate the bean when no specific bean descriptor is selected.
     */
    private BeanDescriptor defaultDescriptor;
}
