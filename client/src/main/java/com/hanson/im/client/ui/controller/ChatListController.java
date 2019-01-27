package com.hanson.im.client.ui.controller;

import com.hanson.im.client.logic.MessageCache;
import com.hanson.im.client.ui.ControlledStage;
import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.StageController;
import com.hanson.im.client.ui.base.ShowMessage;
import com.hanson.im.client.ui.base.UiBaseService;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.body.EncryptText;
import com.hanson.im.common.protocol.body.UserInfo;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Deque;

/**
 * @author hanson
 * @Date 2019/1/23
 * @Description:
 */
public class ChatListController {

    private static ChatListController controller = new ChatListController();

    public static ChatListController getController() {
        return controller;
    }

    public void addChatUser(UserInfo user) {
        StageController stageController = UiBaseService.INSTANCE.getStageController();
        ControlledStage chatViewController = stageController.getController(R.id.ChatView);
        Stage statge = chatViewController.getMyStage();

        ListView<Node> listView = (ListView<Node>) statge.getScene().getRoot().lookup("#chatList");

        Pane userPanel = stageController.load(R.layout.ChatterView, Pane.class);
        Label userId = (Label) userPanel.lookup("#userId");
        Label userName = (Label) userPanel.lookup("#userName");
        userId.setText(user.getId());
        userName.setText(user.getUserName());
        listView.getItems().add(userPanel);

        userPanel.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 1) {
                Label idLabel = (Label) statge.getScene().getRoot().lookup("#chatId");
                Label nameLabel = (Label) statge.getScene().getRoot().lookup("#chatName");
                idLabel.setText(user.getId());
                nameLabel.setText(user.getUserName());
                loadMessage(user.getId());
            }
        });

    }

    public void loadMessage(String session) {
        Deque<ShowMessage> deque = MessageCache.getMessageCache().getMessageQueue(session);

        StageController stageController = UiBaseService.INSTANCE.getStageController();
        ControlledStage chatViewController = stageController.getController(R.id.ChatView);
        Stage statge = chatViewController.getMyStage();

        TextArea textArea = (TextArea)statge.getScene().getRoot().lookup("#chatRecord");
        textArea.clear();

        deque.iterator().forEachRemaining(message -> {
            textArea.appendText(String.format("%s:%s\n",message.getUserName(),message.getContent()));
        });
    }

}
