package com.hanson.im.server.config;

import com.hanson.im.server.service.UserCache;
import com.hanson.im.server.service.impl.LocalUserCache;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Getter
@Component
@ConfigurationProperties(prefix = "config")
public class ServerAppConfig {

    @Value("${config.userCacheType}")
    private String userCacheType;

    @Value("${config.serverId}")
    private String serverId;


    @Bean
    public UserCache userCache() {
        if("redis".equalsIgnoreCase(userCacheType)){
            return null;
        }
        return new LocalUserCache();
    }

}
