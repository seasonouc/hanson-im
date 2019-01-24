package com.hanson.im.client.ui.controller;

import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.StageController;
import com.hanson.im.client.ui.base.UiBaseService;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.testng.annotations.Test;


/**
 * @author hanson
 * @Date 2019/1/21
 * @Description:
 */
public class ChatViewControllerTest extends Application{


    @Override
    public void start(Stage primaryStage) {
        StageController stageController = UiBaseService.INSTANCE.getStageController();
        stageController.setPrimaryStage("root", primaryStage);

        Stage chatStage = stageController.loadStage(R.id.ChatView,R.layout.ChatView, StageStyle.UNDECORATED);
        chatStage.setTitle("chat");
        stageController.setStage(R.id.ChatView);

        Pane userPanel = stageController.load(R.layout.UserView, Pane.class);
        Label userLabel = (Label)userPanel.lookup("#userName");
        userLabel.setText("hello");

        ListView<Node> listView = (ListView<Node>)chatStage.getScene().getRoot().lookup("#chatList");

        listView.getItems().add(userPanel);

    }

    public static void main(String args[]){
        launch();
    }
}