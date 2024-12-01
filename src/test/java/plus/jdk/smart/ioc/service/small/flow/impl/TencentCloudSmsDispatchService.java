package plus.jdk.smart.ioc.service.small.flow.impl;

import cn.hutool.core.lang.generator.ObjectIdGenerator;
import plus.jdk.smart.ioc.annotations.ConditionOnRule;
import plus.jdk.smart.ioc.annotations.SmartService;
import plus.jdk.smart.ioc.model.DispatchContext;
import plus.jdk.smart.ioc.service.small.flow.SmsDispatchService;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过腾讯云下发短信
 */
@Slf4j
@SmartService(group = SmsDispatchService.class)
public class TencentCloudSmsDispatchService implements SmsDispatchService {
    @Override
    @ConditionOnRule("args.dispatchContext.getName() == 'tencent'")
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by tencent cloud");
        dispatchContext.setReceipt(String.format("tencent-%s", new ObjectIdGenerator().next()));
        return true;
    }
}
