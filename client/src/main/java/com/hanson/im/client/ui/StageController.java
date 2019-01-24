package com.hanson.im.client.ui;

import com.hanson.im.client.logic.EventListener;
import com.hanson.im.client.logic.MessageCache;
import com.hanson.im.client.network.http.HttpUitl;
import com.hanson.im.client.ui.base.ShowMessage;
import com.hanson.im.client.ui.controller.ChatListController;
import com.hanson.im.client.ui.controller.UserListController;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.body.EncryptText;
import com.hanson.im.common.protocol.body.UserInfo;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StageController implements EventListener {

    private Map<String, Stage> stages = new HashMap<>();

    private Map<String, ControlledStage> controllers = new HashMap<>();

    public void addStage(String name, Stage stage) {
        this.stages.put(name, stage);
    }

    public Stage getStageBy(String name) {
        return this.stages.get(name);
    }

    public void setPrimaryStage(String name, Stage stage) {
        this.addStage(name, stage);
    }

    public Stage loadStage(String name, String resource, StageStyle... styles) {
        Stage result = null;
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setResources(ResourceBundle.getBundle("i18n/message"));
            Control tmpPane = (Control) loader.load();
            ControlledStage controlledStage = (ControlledStage) loader.getController();
            this.controllers.put(name, controlledStage);
            Scene tmpScene = new Scene(tmpPane);
            result = new Stage();
            result.setScene(tmpScene);

            for (StageStyle style : styles) {
                result.initStyle(style);
            }
            this.addStage(name, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T load(String resource, Class<T> clazz) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            FXMLLoader loader = new FXMLLoader(url);
            return (T) loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T load(String resource, Class<T> clazz, ResourceBundle resources) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            return (T) FXMLLoader.load(url, resources);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Stage setStage(String name) {
        Stage stage = this.getStageBy(name);
        if (stage == null) {
            return null;
        }
        stage.show();
        return stage;
    }

    public boolean switchStage(String toShow, String toClose) {
        getStageBy(toClose).close();
        setStage(toShow);

        return true;
    }

    public void closeStage(String name) {
        Stage target = getStageBy(name);
        target.close();
    }

    public boolean unloadStage(String name) {
        return this.stages.remove(name) != null;
    }

    public ControlledStage getController(String name) {
        return this.controllers.get(name);
    }

    public void runTaskInFxThread(Runnable task) {
        Platform.runLater(task);
    }

    @Override
    public void loginCall(boolean result) {
        Platform.runLater(() -> switchStage(R.id.ChatView, R.id.LoginView));
        List<UserInfo> list = HttpUitl.getHttUtil().getOnlineUserList();
        if (list != null&&list.size() != 0)
        UserListController.getController().refeshUserList(list);
    }

    @Override
    public void receivCall(Message message) {
        EncryptText body = (EncryptText)message.getBody().getData();
        ShowMessage showMessage = new ShowMessage();
        showMessage.setUserName(body.getUserName());
        showMessage.setSenderId(message.getHeader().getFrom());
        showMessage.setContent(body.getContent());
        showMessage.setSessionId(body.getSessionId());
        MessageCache.getMessageCache().addMeesage(body.getSessionId(),showMessage);
    }

    @Override
    public void buildChannelCall(String sessionId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(sessionId);
        userInfo.setUserName("hanson");
        Platform.runLater(()->ChatListController.getController().addChatUser(userInfo));
    }

}
