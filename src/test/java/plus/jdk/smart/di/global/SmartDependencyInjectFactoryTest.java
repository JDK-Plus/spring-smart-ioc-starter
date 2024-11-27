package plus.jdk.smart.di.global;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import plus.jdk.smart.di.model.DispatchContext;

import javax.annotation.Resource;
import javax.script.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = TomcatLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SmartDependencyInjectFactoryTest {

    /**
     * 智能依赖注入工厂实例，用于执行Lua策略评估。
     */
    @Resource
    private SmartDependencyInjectFactory smartDependencyInjectFactory;

    private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    @Test
    public void testScriptEngine() throws InterruptedException, ScriptException {
        DispatchContext dispatchContext = new DispatchContext(){};
        dispatchContext.setName("jack");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("luaj");
        stopWatch.stop();
        log.info("getEngineByName: {}", stopWatch.getTotalTimeMillis());

        String script = "result = dispatchContext:getName() == 'jack'";


        scriptEngine.put("dispatchContext", dispatchContext);
//        stopWatch = new StopWatch();
//        stopWatch.start();
//        // 大约 30毫秒
//        scriptEngine.eval(script);
//        scriptEngine.get("result");
//        stopWatch.stop();
//        log.info("scriptEngine eval cost:{}", stopWatch.getTotalTimeMillis());

        ScriptContext context = scriptEngine.getContext();
//        context.removeAttribute()

        // 试试编译后的
        stopWatch = new StopWatch();
        stopWatch.start();
        CompiledScript compiledScript = ((Compilable) scriptEngine).compile(script);
        stopWatch.stop();
        log.info("compiledScript compile cost {}ms", stopWatch.getTotalTimeMillis());

        stopWatch = new StopWatch();
        stopWatch.start();
        compiledScript.eval(scriptEngine.getContext());
        stopWatch.stop();

        log.info("compiledScript eval cost:{}", stopWatch.getTotalTimeMillis());

        scriptEngine.getContext().setReader(null);
        scriptEngine.getContext().setWriter(null);
        scriptEngine.getContext().setErrorWriter(null);
        scriptEngine.setBindings(new SimpleBindings(), ScriptContext.ENGINE_SCOPE);
        scriptEngine.setBindings(new SimpleBindings(), ScriptContext.GLOBAL_SCOPE);
        log.info("finished");

    }

    @Test
    public void testJNLua() {

    }


    @Test
    public void evalJsePlatform() {

        // 这里耗时最大
        Globals globals = JsePlatform.standardGlobals();

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