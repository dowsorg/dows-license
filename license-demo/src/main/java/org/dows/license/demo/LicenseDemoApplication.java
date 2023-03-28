package org.dows.license.demo;

import lombok.extern.slf4j.Slf4j;
import org.dows.framework.api.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@SpringBootApplication(scanBasePackages = "org.dows.license")
public class LicenseDemoApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(LicenseDemoApplication.class);
        application.run(args);
    }

    /**
     * 模拟验证
     */
    @PostMapping("/check")
    @ResponseBody
    public Response test(@RequestParam(required = true) String username, @RequestParam(required = true) String password) {
        log.info("用户名：{}，密码：{}", username, password);
        return Response.ok();
    }
}
