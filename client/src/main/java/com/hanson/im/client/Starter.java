package com.hanson.im.client;

import com.hanson.im.client.config.ClientConfig;
import com.hanson.im.client.logic.LogicController;
import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.StageController;
import com.hanson.im.client.ui.base.UiBaseService;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author hanson
 * @Date 2019/1/16
 * @Description:
 */
public class Starter  extends Application{


    @Override
    public void start(Stage primaryStage) throws Exception {
        LogicController logicController = LogicController.getController();
        logicController.setInetSocketAddress(ClientConfig.server_address,ClientConfig.server_port);
        logicController.connect();

        StageController stageController = UiBaseService.INSTANCE.getStageController();
        stageController.setPrimaryStage("root", primaryStage);

        logicController.setEventListener(stageController);

        Stage registerStage = stageController.loadStage(R.id.RegisterView,R.layout.RegisterView,StageStyle.UNDECORATED);
        registerStage.setTitle("register");
        Stage chatStage = stageController.loadStage(R.id.ChatView,R.layout.ChatView,StageStyle.UNDECORATED);
        chatStage.setTitle("chat");

        Stage loginStage = stageController.loadStage(R.id.LoginView,R.layout.LoginView, StageStyle.UNDECORATED);
        loginStage.setTitle("login");
        stageController.setStage(R.id.LoginView);
    }

    public static void main(String args[]){
        launch();
    }
}
