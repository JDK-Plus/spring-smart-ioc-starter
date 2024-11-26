package plus.jdk.smart.di.annotations;

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
     * 获取字符串值。
     * @return 字符串值。
     */
    String value() default "";

    /**
     * 获取分组信息, 会注册一个beanName是这个值的 bean 实例。
     * @return 分组信息，若未设置则返回空字符串。
     */
    Class<?> group();
}
