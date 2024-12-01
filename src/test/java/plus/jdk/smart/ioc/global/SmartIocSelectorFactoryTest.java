package plus.jdk.smart.ioc.global;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
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

    @Test
    public void evalExpressionTest() {
        DispatchContext dispatchContext = new DispatchContext() {};
        dispatchContext.setName("jack");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String expression = "dispatchContext.getName() == \"jack\".substring(1)";
        Map<String, Object> params = new HashMap<>();
        params.put("dispatchContext", dispatchContext);

        Object object = smartIocSelectorFactory.evalExpression(expression, params, "testBean", "name");
        stopWatch.stop();
        log.info("evalExpression cost {}ms", stopWatch.getTotalTimeMillis());
    }
}