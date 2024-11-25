package plus.jdk.smart.di.service.impl;

import plus.jdk.smart.di.annotations.SmartService;
import plus.jdk.smart.di.constant.SmsProvider;
import plus.jdk.smart.di.model.DispatchContext;
import plus.jdk.smart.di.service.SmsDispatchService;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过腾讯云下发短信
 */
@Slf4j
@SmartService(group = SmsDispatchService.class)
public class TencentCloudSmsDispatchService implements SmsDispatchService {
    @Override
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by tencent cloud");
        dispatchContext.setProvider(SmsProvider.tencent_cloud);
        return true;
    }
}
