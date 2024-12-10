package plus.jdk.smart.ioc.global;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import plus.jdk.smart.ioc.TomcatLauncher;
import plus.jdk.smart.ioc.model.DispatchContext;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TomcatLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SmartIocSelectorFactoryTest {

    /**
     * Smart dependency injection factory instance for performing jexl policy evaluation.
     */
    @Resource
    private SmartIocSelectorFactory smartIocSelectorFactory;

    /**
     * Global intelligent IOC context instance, used to obtain and manage global configuration information.
     */
    @Resource
    private GlobalSmartIocContext globalSmartIocContext;

    @Test
    public void evalExpressionTest() {
        // Test the magic jexl.eval() function
        Map<String, Object> args = new HashMap<>();

        globalSmartIocContext.registerGlobalVar("testScript", "1 + 2");
        Assert.assertEquals(smartIocSelectorFactory.evalExpression("jexl.eval(global.testScript)", args, null, null), 3);

        args.put("c", 4);
        globalSmartIocContext.registerGlobalVar("testScript", "1 + 2");
        Assert.assertEquals(smartIocSelectorFactory.evalExpression("jexl.eval(global.testScript) < args.c", args, null, null), true);
        Assert.assertEquals(smartIocSelectorFactory.evalExpression("jexl.eval(global.testScript) > args.c", args, null, null), false);
    }
}