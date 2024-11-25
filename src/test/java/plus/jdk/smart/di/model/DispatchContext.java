package plus.jdk.smart.di.model;

import plus.jdk.smart.di.constant.SmsProvider;
import lombok.Data;

@Data
public abstract class DispatchContext {

    /**
     * 手机号码，用于发送短信。
     */
    private String mobile;

    /**
     * 短信服务提供商的标识符。
     */
    private SmsProvider provider;
}
