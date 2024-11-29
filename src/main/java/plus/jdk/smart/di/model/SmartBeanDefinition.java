package plus.jdk.smart.di.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * SmartBeanDefinition类用于管理一组Bean描述符以及一个默认的Bean描述符
 * 它主要应用于需要动态决定Bean实例化配置的场景
 */
@Data
public class SmartBeanDefinition {

    /**
     * 存储一组Bean描述符，用于描述不同的Bean实例化配置
     */
    private List<BeanDescriptor> beanDescriptors = new ArrayList<>();

    /**
     * 默认的Bean描述符，当没有特定Bean描述符被选择时，将使用这个描述符来实例化Bean
     */
    private BeanDescriptor defaultDescriptor;
}
