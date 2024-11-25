package plus.jdk.smart.di.service.impl;

import plus.jdk.smart.di.annotations.SmartService;
import plus.jdk.smart.di.constant.SmsProvider;
import plus.jdk.smart.di.model.DispatchContext;
import plus.jdk.smart.di.service.SmsDispatchService;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过京东云下发短信
 */
@Slf4j
@SmartService(group = SmsDispatchService.class)
public class JdCloudSmsDispatchService implements SmsDispatchService {
    @Override
    public Boolean dispatchMessage(DispatchContext dispatchContext) {
        log.info("dispatchMessage by jd cloud");
        dispatchContext.setProvider(SmsProvider.jd_cloud);
        return true;
    }
}
