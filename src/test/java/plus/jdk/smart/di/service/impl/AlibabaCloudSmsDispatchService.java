package plus.jdk.smart.di.service.impl;

import cn.hutool.core.lang.generator.ObjectIdGenerator;
import plus.jdk.smart.di.annotations.ConditionRule;
import plus.jdk.smart.di.annotations.SmartService;
import plus.jdk.smart.di.constant.SmsProvider;
import plus.jdk.smart.di.model.DispatchContext;
import plus.jdk.smart.di.service.SmsDispatchService;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过阿里云下发短信
 */
@Slf4j
@SmartService(group = SmsDispatchService.class)
public class AlibabaCloudSmsDispatchService implements SmsDispatchService {
    @Override
    @ConditionRule("args.dispatchContext.getName() == 'ali'")
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by alibaba cloud");
        dispatchContext.setReceipt(String.format("alibaba-%s", new ObjectIdGenerator().next()));
        return true;
    }
}
