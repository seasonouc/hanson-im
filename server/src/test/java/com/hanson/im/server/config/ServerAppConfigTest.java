package com.hanson.im.server.config;

import com.hanson.im.server.service.UserCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerAppConfig.class)
public class ServerAppConfigTest {

    @Autowired
    private ServerAppConfig serverAppConfig;

    @Autowired
    private UserCache userCache;

    @Test
    public void testconfig(){
        Assert.assertNotNull(serverAppConfig);
    }

    @Test
    public void testUserCache(){
        Assert.assertNotNull(userCache);
    }
}