package plus.jdk.smart.di.service.impl;

import cn.hutool.core.lang.generator.ObjectIdGenerator;
import lombok.extern.slf4j.Slf4j;
import plus.jdk.smart.di.annotations.ConditionRule;
import plus.jdk.smart.di.annotations.SmartService;
import plus.jdk.smart.di.model.DispatchContext;
import plus.jdk.smart.di.service.SmsDispatchService;

/**
 * 默认的短信发送
 */
@Slf4j
@SmartService(group = SmsDispatchService.class, primary = true)
public class DefaultSmsDispatchService implements SmsDispatchService {
    @Override
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by alibaba cloud");
        dispatchContext.setReceipt(String.format("alibaba-%s", new ObjectIdGenerator().next()));
        return true;
    }
}
