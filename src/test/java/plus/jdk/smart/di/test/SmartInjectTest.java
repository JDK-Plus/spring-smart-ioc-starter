package plus.jdk.smart.di.test;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import plus.jdk.smart.di.model.DispatchContext;
import plus.jdk.smart.di.service.SmsDispatchService;
import plus.jdk.smart.di.service.impl.AlibabaCloudSmsDispatchService;
import plus.jdk.smart.di.service.impl.JdCloudSmsDispatchService;
import plus.jdk.smart.di.service.impl.TencentCloudSmsDispatchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import plus.jdk.smart.di.TomcatLauncher;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TomcatLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SmartInjectTest {


    /**
     * 提供腾讯云短信派发服务的实例。
     */
    @Resource
    private TencentCloudSmsDispatchService tencentCloudSmsDispatchService;

    /**
     * 提供阿里云短信派发服务的实例。
     */
    @Resource
    private AlibabaCloudSmsDispatchService alibabaCloudSmsDispatchService;

    /**
     * 提供京东云短信派发服务的实例。
     */
    @Resource
    private JdCloudSmsDispatchService jdCloudSmsDispatchService;

    /**
     * 注入的短信派发服务实例，用于在测试中模拟短信派发。
     */
    @Resource
    private SmsDispatchService smsDispatchService;

    @Test
    public void hello() throws InterruptedException {
        DispatchContext context = new DispatchContext() {};
        smsDispatchService.dispatchMessage(context);
        log.info("hello");
    }
}
