package com.hanson.im.client.ui.controller;

import com.hanson.im.client.logic.LogicController;
import com.hanson.im.client.network.http.HttpUitl;
import com.hanson.im.client.ui.ControlledStage;
import com.hanson.im.client.ui.MessageAppender;
import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.StageController;
import com.hanson.im.client.ui.base.UiBaseService;
import com.hanson.im.common.protocol.body.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * @author hanson
 * @Date 2019/1/21
 * @Description:
 */
public class ChatViewController implements ControlledStage, MessageAppender {

    @FXML
    private TextArea sendContent;

    @Override
    public Stage getMyStage() {
        StageController stageController = UiBaseService.INSTANCE.getStageController();
        return stageController.getStageBy(R.id.ChatView);
    }

    @FXML
    private void clearSend() {
        sendContent.clear();
    }

    @FXML
    private void close() {
        System.exit(1);
    }


    @Override
    public void appenMessge(String message) {

    }

    @FXML
    private void sendMessage(){

        StageController stageController = UiBaseService.INSTANCE.getStageController();
        ControlledStage chatViewController = stageController.getController(R.id.ChatView);
        Stage statge = chatViewController.getMyStage();

        TextArea textArea = (TextArea)statge.getScene().getRoot().lookup("#sendContent");
        Label label = (Label) statge.getScene().getRoot().lookup("#chatId");
        String sessionId = label.getText();
        String content = textArea.getText();

        LogicController.getController().sendMessage(sessionId,content);
    }
}
