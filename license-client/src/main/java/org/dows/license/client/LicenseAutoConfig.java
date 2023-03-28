package org.dows.license.client;

import lombok.RequiredArgsConstructor;
import org.dows.framework.api.util.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@PropertySource(value = "classpath:application-license.yml", factory = YamlPropertySourceFactory.class)
@Configuration
@EnableConfigurationProperties(LicenseProperties.class)
public class LicenseAutoConfig implements WebMvcConfigurer {

    private final LicenseProperties licenseProperties;

    @Bean
    public LicenseCheckInterceptor licenseCheckInterceptor() {
        return new LicenseCheckInterceptor();
    }

    @Bean
    public LicenseCheckListener licenseCheckListener() {
        return new LicenseCheckListener(licenseProperties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加要拦截的url
        registry.addInterceptor(licenseCheckInterceptor())
                .addPathPatterns("/**");// 拦截的路径
//                .excludePathPatterns("/admin/login");// 放行的路径
    }

}
