package plus.jdk.smart.ioc.service;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import plus.jdk.smart.ioc.TomcatLauncher;
import plus.jdk.smart.ioc.global.Advised;
import plus.jdk.smart.ioc.model.DispatchContext;
import plus.jdk.smart.ioc.service.small.flow.SmsDispatchService;
import plus.jdk.smart.ioc.service.small.flow.impl.AlibabaCloudSmsDispatchService;
import plus.jdk.smart.ioc.service.small.flow.impl.JdCloudSmsDispatchService;
import plus.jdk.smart.ioc.service.small.flow.impl.TencentCloudSmsDispatchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TomcatLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SmartFlowByMethodArgsTest {

    /**
     * The injected SMS delivery service instance is used to simulate SMS delivery in tests.
     */
    @Resource
    private SmsDispatchService smsDispatchService;

    @Test
    public void testSmallFlow() throws InterruptedException {

        // The input name parameter is ali, and the Alibaba Cloud interface is called to send text messages.
        DispatchContext context = new DispatchContext() {};
        context.setName("ali");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("ali"));

        // The input name parameter is tencent, and the interface of Tencent Cloud is called to send text messages.
        context.setName("tencent");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("tencent"));

        // The input name parameter is jd, and the interface of JD Cloud is called to send text messages.
        context.setName("jd");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("jd"));

        // The input name parameter is other, and the default main bean implementation is called to send text messages.
        context.setName("other");
        Assert.assertTrue(smsDispatchService.dispatchMessage(context));
        Assert.assertTrue(context.getReceipt().startsWith("tencent"));

        // test the getOriginTarget method implemented by the proxy class
        Assert.assertTrue(smsDispatchService instanceof Advised);
        Assert.assertNotNull(((Advised) smsDispatchService).getOriginTarget());

        // test the toString method implemented by the proxy class
        Assert.assertTrue(smsDispatchService.toString().contains("SDI"));
        log.info("hello");
    }
}
