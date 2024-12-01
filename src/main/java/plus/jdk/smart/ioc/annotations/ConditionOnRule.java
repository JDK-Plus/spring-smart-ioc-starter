package plus.jdk.smart.ioc.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionOnRule {
    /**
     * The expression used to determine whether to use this bean instance can be customized according
     * to small traffic scenarios and service degradation scenarios.
     */
    String value();
}
