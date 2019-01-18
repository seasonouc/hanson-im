package com.hanson.im.client.chat;

import com.hanson.im.client.handler.BuildChannelListenner;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/16
 * @Description:
 */
@Slf4j
public class ControllerTest {

    @Test
    public void testLogin() {
        Controller controller = new Controller();

        controller.setInetSocketAddress("localhost", 6377);
        controller.connect();
        controller.setIdAndName("321321321321321", "hanson");
        controller.login();

        int waitTimes = 0;
        while (!controller.getLogin()) {
            waitTimes++;
            if (waitTimes > 10) {
                break;
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Assert.assertTrue(controller.getLogin());
    }


    @Test
    public void testBuildEncryptChannel() throws InterruptedException {
        Controller controller1 = new Controller();

        controller1.setInetSocketAddress("localhost", 6377);
        controller1.connect();
        controller1.setIdAndName("123", "hanson");
        controller1.login();

        log.info("test generate id:{}",controller1.generateSessionId());

        Controller controller2 = new Controller();
        controller2.setInetSocketAddress("localhost", 6377);
        controller2.connect();
        controller2.setIdAndName("456", "hanson");
        controller2.login();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller1.setListenner(new BuildChannelListenner() {
            @Override
            public void buildChannle(String sessionId) {
                log.info("controller1 build channel connection with session :{}",sessionId);
                controller1.sendMessage(sessionId,"hello world").whenComplete((result,error)->{log.info("send text message result :{}",result);});
            }
        });
        controller2.setListenner(new BuildChannelListenner() {
            @Override
            public void buildChannle(String sessionId) {
                log.info("controller2 build channel connection with session :{}",sessionId);
            }
        });

        if (controller1.getLogin() && controller2.getLogin()) {
            List<String> userList = new ArrayList<>();
            userList.add("456");
            controller1.buildEncryptChannle(userList).whenComplete((result,error)->{
                log.info("controller1 send build mesasge result:{}",result);
            });

        }

        Thread.sleep(50000);

    }

}