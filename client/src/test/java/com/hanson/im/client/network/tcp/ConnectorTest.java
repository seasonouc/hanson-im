package com.hanson.im.client.network.tcp;

import com.hanson.im.common.protocol.body.LoginRequest;
import io.netty.channel.ChannelFuture;
import org.testng.annotations.Test;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
public class ConnectorTest {

    @Test
    public void testStartAndLogin() throws Exception {

        Connector connector = new Connector();
        connector.setAddress("localhost",6377)
                .start()
                .whenComplete((value,error)->{
                    System.out.println("complete to connect");
//                    Assert.assertTrue(value);
                });
        LoginRequest request = new LoginRequest();
        request.setUserId("321321321321321");
        request.setUserName("hanson");
        ChannelFuture future = connector.login(request);
        future.addListener((chanelFuture)-> {
            System.out.println("login sucess");
        });
        Thread.sleep(10000);
    }
}