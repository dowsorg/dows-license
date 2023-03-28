package org.dows.license.client;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dows.license.api.LicenseVerifyParam;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 在项目启动时安装证书
 */
@Slf4j
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {
//    /**
//     * 证书subject
//     */
//    private String subject;
//
//    /**
//     * 公钥别称
//     */
//    private String publicAlias;
//
//    /**
//     * 访问公钥库的密码
//     */
//    private String storePass;
//
    /**
     * 证书生成路径
     */
    private String licensePath;
    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;

    private LicenseProperties licenseProperties;

    public LicenseCheckListener(LicenseProperties licenseProperties) {
        this.licenseProperties = licenseProperties;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent
        ApplicationContext context = event.getApplicationContext().getParent();
        if (context == null) {
            licensePath = licenseProperties.getLicensePath();
            publicKeysStorePath = licenseProperties.getPublicKeysStorePath();
            if (StrUtil.isBlank(licensePath)) {
                String uhome = System.getProperty("user.home");
                licensePath = uhome;
            }
            if (StrUtil.isBlank(publicKeysStorePath)) {
                try {
                    File file = ResourceUtils.getFile("classpath:publicCerts.keystore");
                    publicKeysStorePath = file.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (StrUtil.isNotBlank(licensePath)) {
                log.info("++++++++ 开始安装证书 ++++++++");
                LicenseVerifyParam param = new LicenseVerifyParam();
                param.setSubject(licenseProperties.getSubject());
                param.setPublicAlias(licenseProperties.getPublicAlias());
                param.setStorePass(licenseProperties.getStorePass());
                param.setLicensePath(licensePath);
                param.setPublicKeysStorePath(publicKeysStorePath);
                LicenseVerify licenseVerify = new LicenseVerify();
                //安装证书
                licenseVerify.install(param);
                log.info("++++++++ 证书安装结束 ++++++++");
            }
        }
    }
}
