package com.hanson.im.client.logic;

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
public class LogicControllerTest {

    @Test
    public void testLogin() {
        LogicController logicController = new LogicController();

        logicController.setInetSocketAddress("localhost", 6377);
        logicController.connect();
        logicController.setIdAndName("321321321321321", "hanson");
        logicController.login();

        int waitTimes = 0;
        while (!logicController.getLogin()) {
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

        Assert.assertTrue(logicController.getLogin());
    }


    @Test
    public void testBuildEncryptChannel() throws InterruptedException {
        LogicController logicController1 = new LogicController();

        logicController1.setInetSocketAddress("localhost", 6377);
        logicController1.connect();
        logicController1.setIdAndName("123", "hanson");
        logicController1.login();

        log.info("test generate id:{}", logicController1.generateSessionId());

        LogicController logicController2 = new LogicController();
        logicController2.setInetSocketAddress("localhost", 6377);
        logicController2.connect();
        logicController2.setIdAndName("456", "hanson");
        logicController2.login();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logicController1.setListenner(new BuildChannelListenner() {
            @Override
            public void buildChannle(String sessionId) {
                log.info("controller1 build channel connection with session :{}",sessionId);
                logicController1.sendMessage(sessionId,"hello world").whenComplete((result, error)->{log.info("send text message result :{}",result);});
            }
        });
        logicController2.setListenner(new BuildChannelListenner() {
            @Override
            public void buildChannle(String sessionId) {
                log.info("controller2 build channel connection with session :{}",sessionId);
            }
        });

        if (logicController1.getLogin() && logicController2.getLogin()) {
            List<String> userList = new ArrayList<>();
            userList.add("456");
            logicController1.buildEncryptChannle(userList).whenComplete((result, error)->{
                log.info("controller1 send build mesasge result:{}",result);
            });

        }

        Thread.sleep(5000);

    }

}