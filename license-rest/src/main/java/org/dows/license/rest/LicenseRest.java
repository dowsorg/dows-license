package org.dows.license.rest;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.dows.license.api.*;
import org.dows.license.sdk.LicenseCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 用于生成证书文件，不能放在给客户部署的代码里
 */
@RestController
@RequestMapping("/license")
@Api(tags = "生成证书文件")
public class LicenseRest {

    /**
     * 证书生成路径
     */
    @Value("${dows.license.licensePath}")
    private String licensePath;


    /**
     * 获取服务器硬件信息
     *
     * @param osName 操作系统类型，如果为空则自动判断
     * @return
     */
    @PostMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "获取服务器硬件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "osName", value = "操作系统类型（不填默认按Windows系统处理）")
    })
    public LicenseCheckModel getServerInfos(String osName) {
        //操作系统类型
        if (StrUtil.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        return abstractServerInfos.getServerInfos();
    }


    /**
     * 生成证书
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "生成证书")
    public Map<String, Object> generateLicense(@RequestBody(required = true) LicenseCreatorParam param) {
        Map<String, Object> resultMap = new HashMap<>(2);

        if (StrUtil.isBlank(param.getLicensePath())) {
            param.setLicensePath(licensePath);
        }
        try {
            Files.createDirectories(Path.of(param.getLicensePath().substring(0,param.getLicensePath().lastIndexOf("/"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (StrUtil.isBlank(param.getPrivateKeysStorePath())) {
            try {
                File file = ResourceUtils.getFile("classpath:privateKeys.keystore");
                param.setPrivateKeysStorePath(file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        if (result) {
            resultMap.put("result", "ok");
            resultMap.put("msg", param);
        } else {
            resultMap.put("result", "error");
            resultMap.put("msg", "证书文件生成失败！");
        }

        return resultMap;
    }

}
