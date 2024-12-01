package plus.jdk.smart.ioc.model;

import lombok.Data;

@Data
public abstract class DispatchContext {

    /**
     * The name of the context.
     */
    private String name;

    /**
     * Mobile phone number used to send text messages.
     */
    private String mobile;

    /**
     * Receipt number, used to record transaction information.
     */
    private String receipt;
}
