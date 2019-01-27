package com.hanson.im.client.ui.controller;

import com.hanson.im.client.logic.LogicController;
import com.hanson.im.client.ui.ControlledStage;
import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.StageController;
import com.hanson.im.client.ui.base.UiBaseService;
import com.hanson.im.common.protocol.body.UserInfo;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

/**
 * @author hanson
 * @Date 2019/1/22
 * @Description:
 */
public class UserListController implements Initializable{

    private static UserListController controller = new UserListController();

    public static UserListController getController() {
        return controller;
    }

    Map<String,UserInfo> userInfoMap = new HashMap<>();

    public void refreshUserList(List<UserInfo> userList){
        userInfoMap.clear();
        userList.forEach(userInfo -> userInfoMap.put(userInfo.getId(),userInfo));
    }

    public UserInfo getUserInfo(String id){
        return userInfoMap.get(id);
    }

    public void refeshUserList(List<UserInfo> userList) {
        if(userList == null||userList.size() == 0){
            return;
        }
        StageController stageController = UiBaseService.INSTANCE.getStageController();
        ControlledStage chatViewController = stageController.getController(R.id.ChatView);
        Stage statge = chatViewController.getMyStage();

        ListView<Node> listView = (ListView<Node>) statge.getScene().getRoot().lookup("#userList");

        userList.forEach(user -> {
            if(user.getId().equals(LogicController.getController().getMyId())){
                return;
            }
            Pane userPanel = stageController.load(R.layout.UserView, Pane.class);
            Label userId = (Label) userPanel.lookup("#id");
            Label userName = (Label) userPanel.lookup("#userName");
            userId.setText(user.getId());
            userName.setText(user.getUserName());
            listView.getItems().add(userPanel);
        });
        bindDoubleClick(listView);
    }

    private void bindDoubleClick(ListView<Node> list){
        list.setOnMouseClicked(event->{
            if(event.getClickCount() >= 2){
                ListView<Node> view = ( ListView<Node>)event.getSource();
                Node item = view.getSelectionModel().getSelectedItem();
                if(item == null){
                    return;
                }
                Pane pane = (Pane)item;
                Label userId = (Label)pane.lookup("#id");
                Label userName = (Label)pane.lookup("#userName");
                String id = userId.getText();
                String name = userName.getText();
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(name);
                userInfo.setId(id);
                List<UserInfo> userIds = new ArrayList<>();
                userIds.add(userInfo);
                LogicController.getController().buildEncryptChannle(userIds);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
