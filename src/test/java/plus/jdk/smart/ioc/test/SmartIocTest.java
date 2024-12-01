package plus.jdk.smart.ioc.test;

import org.junit.Assert;
import plus.jdk.smart.ioc.global.Advised;
import plus.jdk.smart.ioc.model.DispatchContext;
import plus.jdk.smart.ioc.service.small.flow.SmsDispatchService;
import plus.jdk.smart.ioc.service.small.flow.impl.AlibabaCloudSmsDispatchService;
import plus.jdk.smart.ioc.service.small.flow.impl.JdCloudSmsDispatchService;
import plus.jdk.smart.ioc.service.small.flow.impl.TencentCloudSmsDispatchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import plus.jdk.smart.ioc.TomcatLauncher;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TomcatLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SmartIocTest {


    /**
     * Provides examples of Tencent Cloud SMS distribution service.
     */
    @Resource
    private TencentCloudSmsDispatchService tencentCloudSmsDispatchService;

    /**
     * Provides examples of Alibaba Cloud SMS delivery service.
     */
    @Resource
    private AlibabaCloudSmsDispatchService alibabaCloudSmsDispatchService;

    /**
     * Provides examples of JD Cloud SMS distribution service.
     */
    @Resource
    private JdCloudSmsDispatchService jdCloudSmsDispatchService;

    /**
     * The injected SMS delivery service instance is used to simulate SMS delivery in tests.
     */
    @Resource
    private SmsDispatchService smsDispatchService;

    @Test
    public void testEvalExpression() throws InterruptedException {
        DispatchContext context = new DispatchContext() {};
        context.setName("ali");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("ali"));

        context.setName("tencent");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("tencent"));

        context.setName("jd");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("jd"));

        context.setName("other");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("default"));

        Assert.assertTrue(smsDispatchService instanceof Advised);
        Assert.assertNotNull(((Advised) smsDispatchService).getOriginTarget());

        Assert.assertTrue(smsDispatchService.toString().contains("SDI"));
        log.info("hello");
    }
}
