package org.dows.license.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dows.license")
@Data
public class LicenseProperties {

    /**
     * 证书subject
     */
    //@Value("${dows.license.subject}")
    private String subject;

    /**
     * 公钥别称
     */
    //@Value("${dows.license.publicAlias}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    //@Value("${dows.license.storePass}")
    private String storePass;

    /**
     * 证书生成路径
     */
    //@Value("${dows.license.licensePath}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    //@Value("${dows.license.publicKeysStorePath}")
    private String publicKeysStorePath;
}
