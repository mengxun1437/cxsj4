package online.mengxun.server.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "WxApi",
        url = "https://api.weixin.qq.com"
)
public interface WxApiClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/sns/jscode2session?appid={appId}&secret={appSecretKey}&js_code={jsCode}&grant_type=authorization_code&connect_redirect=1"
    )
    String getOpenIdByCode(@PathVariable("appId") String appId,
                         @PathVariable("appSecretKey") String appSecretKey,
                         @PathVariable("jsCode") String jsCode);
}
