package plus.jdk.smart.ioc.service.small.flow.impl;

import cn.hutool.core.lang.generator.ObjectIdGenerator;
import plus.jdk.smart.ioc.annotations.ConditionOnRule;
import plus.jdk.smart.ioc.annotations.SmartService;
import plus.jdk.smart.ioc.model.DispatchContext;
import plus.jdk.smart.ioc.service.small.flow.SmsDispatchService;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过阿里云下发短信
 */
@Slf4j
@SmartService(group = SmsDispatchService.class)
public class AlibabaCloudSmsDispatchService implements SmsDispatchService {
    @Override
    @ConditionOnRule("args.dispatchContext.getName() == 'ali'")
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by alibaba cloud");
        dispatchContext.setReceipt(String.format("alibaba-%s", new ObjectIdGenerator().next()));
        return true;
    }
}
