package org.dows.license.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableKnife4j
@EnableOpenApi
@Configuration
public class SwaggerConfig {

    @Value("${swagger.host:{null}}")
    private String host;
    @Value("${swagger.title:{null}}")
    private String title;
    @Value("${swagger.version:{null}}")
    private String version;
    @Value("${swagger.description:{null}}")
    private String description;
    @Value("${swagger.license:{null}}")
    private String license;
    @Value("${swagger.licenseUrl:{null}}")
    private String licenseUrl;
    @Value("${swagger.contact.name:{null}}")
    private String contactName;
    @Value("${swagger.contact.email:{null}}")
    private String contactEmail;
    @Value("${swagger.contact.url:{null}}")
    private String contactUrl;
    @Value("${swagger.package:{null}}")
    private String packagePath;

    //配置swagger的Docket的bean实例
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                //basePackage("") 指定要扫描的包
                //any 扫描全部
                //none 都不扫描
                //withClassAnnotation(RequestMapping.class) 扫描类上的注解
                //withMethodAnnotation(GetMapping.class) 扫描方法上面的注解 也可以说是扫描请求方式
                .apis(RequestHandlerSelectors.basePackage(packagePath))
                .paths(PathSelectors.any())
                //paths 过滤什么路径
                // .paths(PathSelectors.ant(""))
                .build()
                .groupName("License管理端")
                .host(host);
    }

    private ApiInfo apiInfo() {

        //作者信息
        Contact contact = new Contact(contactName, contactUrl, contactEmail);
        return new ApiInfo(
                title,
                description,
                version,
                contactUrl,
                contact,
                license,
                licenseUrl,
                new ArrayList<>());
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier,
                                                                         ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes,
                                                                         CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties,
                                                                         Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping,
                null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }


}
