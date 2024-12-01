package plus.jdk.smart.ioc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TomcatLauncher {

    @Bean
    public ServletWebServerFactory getServletWebServerFactory() {
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
        // 这里可以添加自定义配置
        // 例如：factory.addContextCustomizers(context -> context.addLifecycleListener(new AprLifecycleListener()));
        return factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(TomcatLauncher.class, args);
    }
}
