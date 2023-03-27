package org.dows.license.client;

import cn.hutool.json.JSONUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


public class LicenseCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseVerify licenseVerify = new LicenseVerify();
        //校验证书是否有效
        boolean verifyResult = licenseVerify.verify();

        if (verifyResult) {
            return true;
        } else {
            response.setCharacterEncoding("utf-8");
            Map<String, String> result = new HashMap<>(1);
            result.put("result", "您的证书无效，请核查服务器是否取得授权或重新申请证书！");
            response.getWriter().write(JSONUtil.toJsonStr(result));
            return false;
        }
    }

}
