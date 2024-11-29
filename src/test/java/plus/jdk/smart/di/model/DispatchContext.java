package plus.jdk.smart.di.model;

import plus.jdk.smart.di.constant.SmsProvider;
import lombok.Data;

@Data
public abstract class DispatchContext {

    /**
     * 上下文的名称。
     */
    private String name;

    /**
     * 手机号码，用于发送短信。
     */
    private String mobile;

    /**
     * 收据号码，用于记录交易信息。
     */
    private String receipt;
}
