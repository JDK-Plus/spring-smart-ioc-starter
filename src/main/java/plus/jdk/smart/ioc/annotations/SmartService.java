package plus.jdk.smart.ioc.annotations;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Bean
@Service
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SmartService {

    /**
     *  Customize the beanName of the current bean, but be careful not to conflict with the interface name.
     */
    String value() default "";

    /**
     * To obtain group information, a bean instance whose beanName is this value will be registered, and at the same time,
     * it will be declared which interface implementation class the current bean is.
     */
    Class<?> group();

    /**
     * Whether it is the main instance,
     * the implementation of this class will be executed when the condition rules on all implementation classes are not hit.。
     */
    boolean primary() default false;
}
