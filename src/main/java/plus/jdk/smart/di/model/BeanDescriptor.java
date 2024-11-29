package plus.jdk.smart.di.model;

import lombok.Builder;
import lombok.Data;
import plus.jdk.smart.di.annotations.SmartService;

/**
 * Bean描述符类，用于封装Bean的相关信息
 * 这包括Bean的名称、对应的类类型、提供的智能服务接口，以及Bean的实例
 */
@Data
@Builder
public class BeanDescriptor {

    /**
     * Bean的名称，用于唯一标识一个Bean
     */
    private String beanName;

    /**
     * Bean对应的类类型
     * 这决定了Bean实例化时的具体类型
     */
    private Class<?> clazz;

    /**
     * 提供的智能服务接口
     * 用于实现高级服务逻辑，如动态配置、健康检查等
     */
    private SmartService smartService;

    /**
     * Bean的实例对象
     * 用于存储Bean的实际运行时实例
     */
    private Object beanInstance;
}
