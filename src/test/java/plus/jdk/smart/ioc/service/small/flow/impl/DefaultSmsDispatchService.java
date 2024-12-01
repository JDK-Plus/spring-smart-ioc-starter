package plus.jdk.smart.ioc.service.small.flow.impl;

import cn.hutool.core.lang.generator.ObjectIdGenerator;
import lombok.extern.slf4j.Slf4j;
import plus.jdk.smart.ioc.annotations.SmartService;
import plus.jdk.smart.ioc.model.DispatchContext;
import plus.jdk.smart.ioc.service.small.flow.SmsDispatchService;

/**
 * 默认的短信发送
 */
@Slf4j
@SmartService(group = SmsDispatchService.class, primary = true)
public class DefaultSmsDispatchService extends TencentCloudSmsDispatchService {
    @Override
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by alibaba cloud");
        dispatchContext.setReceipt(String.format("default-%s", new ObjectIdGenerator().next()));
        return true;
    }
}
