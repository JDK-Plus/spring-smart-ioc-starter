package plus.jdk.smart.di.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionRule {
    /**
     * lua脚本。
     * @return 字符串值。
     */
    String value();
}
