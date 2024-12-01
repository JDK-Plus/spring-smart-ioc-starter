package plus.jdk.smart.ioc.global;


public interface Advised {

    /**
     * A reference to the proxied object.
     */
    Object getOriginTarget();
}
