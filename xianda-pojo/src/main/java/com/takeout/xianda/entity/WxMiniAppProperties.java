package com.takeout.xianda.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMiniAppProperties {
    private String appId;
    private String appSecret;
}
