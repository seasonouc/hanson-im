package com.hanson.im.client.chat;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanson
 * @Date 2019/1/16
 * @Description:
 */
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
    public void testBuildEncryptChannel() {
        Controller controller1 = new Controller();

        controller1.setInetSocketAddress("localhost", 6377);
        controller1.connect();
        controller1.setIdAndName("123", "hanson");
        controller1.login();

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
        if (controller1.getLogin() && controller2.getLogin()) {
            List<String> userList = new ArrayList<>();

            controller1.buildEncryptChannle(userList);
        }
    }
}