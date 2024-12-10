package plus.jdk.smart.ioc.global;

public interface Evalable {
    /**
     * Execute the specified script and return the results。
     */
    Object eval(String script);
}
