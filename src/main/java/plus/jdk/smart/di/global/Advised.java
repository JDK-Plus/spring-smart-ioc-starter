package plus.jdk.smart.di.global;

import lombok.Data;


public interface Advised {

    /**
     * 被代理对象的引用。
     */
    Object getOriginTarget();
}
