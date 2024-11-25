package plus.jdk.smart.di.service;

import plus.jdk.smart.di.model.DispatchContext;

public interface SmsDispatchService {

    /**
     * 发送短信的方法。
     * @param dispatchContext 短信派发上下文，包含了短信的相关信息。
     * @return 发送是否成功。
     */
    Boolean dispatchMessage(DispatchContext dispatchContext);
}
