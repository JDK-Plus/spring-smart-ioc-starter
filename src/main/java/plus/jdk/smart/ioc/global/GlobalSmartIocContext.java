package plus.jdk.smart.ioc.global;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Smart IoC context for obtaining environment variables and parsing expressions.
 */
public class GlobalSmartIocContext {

    /**
     * Stores the configuration properties of SmartIocContext.
     */
    private final Map<String, Object> properties = new ConcurrentHashMap<>();

    /**
     * Register a global variable.
     */
    public void registerGlobalVar(String name, Object value) {
        properties.put(name, value);
    }

    /**
     * Get the value of the global variable based on the variable name.
     */
    public Object getGlobalVar(String name) {
        return properties.get(name);
    }

    /**
     * Get the value of a global variable.
     */
    protected  Map<String, Object>  getGlobalProperties() {
        return properties;
    }
}
