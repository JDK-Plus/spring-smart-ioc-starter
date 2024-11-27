package plus.jdk.smart.di.service.impl;

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
    @ConditionRule("dispatchContext.name == 'jack'")
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by alibaba cloud");
        dispatchContext.setProvider(SmsProvider.ali_cloud);
        return true;
    }
}
