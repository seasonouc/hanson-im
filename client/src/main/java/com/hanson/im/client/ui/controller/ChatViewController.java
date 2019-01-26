package com.hanson.im.client.ui.controller;

import com.hanson.im.client.logic.LogicController;
import com.hanson.im.client.ui.ControlledStage;
import com.hanson.im.client.ui.MessageAppender;
import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.StageController;
import com.hanson.im.client.ui.base.ResourceContainer;
import com.hanson.im.client.ui.base.UiBaseService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


/**
 * @author hanson
 * @Date 2019/1/21
 * @Description:
 */
public class ChatViewController implements ControlledStage, MessageAppender {

    @FXML
    private TextArea sendContent;

    @FXML
    private ImageView closeBtn;

    @FXML
    private ImageView minBtn;

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
    private void sendMessage() {

        StageController stageController = UiBaseService.INSTANCE.getStageController();
        ControlledStage chatViewController = stageController.getController(R.id.ChatView);
        Stage statge = chatViewController.getMyStage();

        TextArea textArea = (TextArea) statge.getScene().getRoot().lookup("#sendContent");
        Label label = (Label) statge.getScene().getRoot().lookup("#chatId");
        String sessionId = label.getText();
        String content = textArea.getText();

        LogicController.getController().sendMessage(sessionId, content);
        ChatListController.getController().loadMessage(sessionId);
        sendContent.clear();
    }

    @FXML
    public void closeEnter() {
        Image image = ResourceContainer.getClose();
        closeBtn.setImage(image);
    }

    @FXML
    public void closeExit() {
        Image image = ResourceContainer.getClose_1();
        closeBtn.setImage(image);
    }

    @FXML
    public void minimum() {
        Stage stage = getMyStage();
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    public void minEnter() {
        Image image = ResourceContainer.getMin();
        minBtn.setImage(image);
    }

    @FXML
    public void minExit() {
        Image image = ResourceContainer.getMin_1();
        minBtn.setImage(image);
    }
}
