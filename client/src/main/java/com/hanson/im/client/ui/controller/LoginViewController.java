package com.hanson.im.client.ui.controller;

import com.hanson.im.client.logic.LogicController;
import com.hanson.im.client.ui.ControlledStage;
import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.StageController;
import com.hanson.im.client.ui.base.UiBaseService;
import com.hanson.im.common.utils.Vailidator;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author hanson
 * @Date 2019/1/18
 * @Description:
 */
public class LoginViewController implements ControlledStage, Initializable {


    @FXML
    private TextField userId;

    @FXML
    private TextField password;

    @FXML
    private Button login;

    @FXML
    private Button register;


    @Override
    public Stage getMyStage() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.disableProperty().bind(Bindings.createBooleanBinding(
                () -> userId.getText().length() == 0 ||
                        !Vailidator.vailidateUserId(userId.getText()) ||
                        password.getText().length() == 0,
                userId.textProperty(),
                password.textProperty()));
    }

    @FXML
    public void onLogin() {
        LogicController.getController().setIdAndName(userId.getText(), userId.getText());
        LogicController.getController().login();
    }

}
