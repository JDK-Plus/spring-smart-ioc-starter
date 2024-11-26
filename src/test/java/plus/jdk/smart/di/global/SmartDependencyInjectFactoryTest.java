package plus.jdk.smart.di.global;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import plus.jdk.smart.di.TomcatLauncher;
import plus.jdk.smart.di.model.DispatchContext;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = TomcatLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SmartDependencyInjectFactoryTest {

    /**
     * 智能依赖注入工厂实例，用于执行Lua策略评估。
     */
    @Resource
    private SmartDependencyInjectFactory smartDependencyInjectFactory;

    ScriptEngineManager mgr = new ScriptEngineManager();

    @Test
    public void test() throws InterruptedException, ScriptException {
        DispatchContext dispatchContext = new DispatchContext(){};
        dispatchContext.setName("jack");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ScriptEngine e = mgr.getEngineByName("lua");
        stopWatch.stop();
        log.info("ScriptEngine: {}", stopWatch.getTotalTimeMillis());
        e.put("dispatchContext", dispatchContext);

        stopWatch = new StopWatch();
        stopWatch.start();
        e.eval("result = dispatchContext:getName() == 'jack'");
        e.get("result");
        stopWatch.stop();
        log.info("cost:{}", stopWatch.getTotalTimeMillis());
    }


    Globals globals = JsePlatform.standardGlobals();

    @Test
    public void evalLuaPolicy() {
        // 创建一个Java对象
        DispatchContext dispatchContext = new DispatchContext(){};
        dispatchContext.setName("jack");

        // 创建参数映射
        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put("dispatchContext", dispatchContext);

        StopWatch stopWatch = new StopWatch();

        stopWatch = new StopWatch();
        stopWatch.start();
        // 将Java对象传递给Lua环境
        LuaTable params = new LuaTable();
        for (String key : argsMap.keySet()) {
            params.set(key, CoerceJavaToLua.coerce(argsMap.get(key)));
        }
        stopWatch.stop();
        log.info("add params, cost {} ms", stopWatch.getTotalTimeMillis());

        stopWatch = new StopWatch();
        stopWatch.start();
        // 将参数表设置到全局环境中
        globals.set("params", params);
        // 加载并执行Lua脚本
        String script = "return params.dispatchContext:getName() == 'jack'";
        LuaValue chunk = globals.load(script, "script");
        stopWatch.stop();
        log.info("globals.load, cost {} ms", stopWatch.getTotalTimeMillis());

        stopWatch = new StopWatch();
        stopWatch.start();
        // 调用Lua脚本
        LuaValue result = chunk.call();
        stopWatch.stop();
        log.info("chunk.call() cost {} ms", stopWatch.getTotalTimeMillis());
    }
}