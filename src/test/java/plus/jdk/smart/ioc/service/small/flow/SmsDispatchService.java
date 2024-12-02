package plus.jdk.smart.ioc.service.small.flow;

import plus.jdk.smart.ioc.model.DispatchContext;

public interface SmsDispatchService {

    /**
     * Ways to send text messages.
     * @param dispatchContext SMS dispatch context, including SMS related information.
     * @return Whether the sending was successful.
     */
    Boolean dispatchMessage(DispatchContext dispatchContext);
}
