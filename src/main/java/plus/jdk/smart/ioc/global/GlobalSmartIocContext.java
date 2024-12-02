package plus.jdk.smart.ioc.global;

import java.util.Properties;

/**
 * Smart IoC context for obtaining environment variables and parsing expressions.
 */
public class GlobalSmartIocContext {

    /**
     * Stores the configuration properties of SmartIocContext.
     */
    private final Properties properties = new Properties();

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
    protected Properties getGlobalProperties() {
        return properties;
    }
}
