package com.hanson.im.client.ui.controller;

import com.hanson.im.client.network.http.HttpUitl;
import com.hanson.im.client.ui.ControlledStage;
import com.hanson.im.client.ui.R;
import com.hanson.im.client.ui.base.UiBaseService;
import com.hanson.im.common.utils.Vailidator;
import com.hanson.im.common.vo.req.RegisterUserReqVO;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author hanson
 * @Date 2019/1/25
 * @Description:
 */
public class RegisterViewController implements ControlledStage,Initializable {

    @FXML
    private TextField userName;

    @FXML
    private TextField userId;

    @FXML
    private PasswordField password;

    @FXML
    private Button register;

    @FXML
    private Button back;

    @Override
    public Stage getMyStage() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        register.disableProperty().bind(Bindings.createBooleanBinding(
                () -> userId.getText().length() == 0 ||
                        !Vailidator.vailidateUserId(userId.getText()) ||
                        password.getText().length() == 0||
                        userName.getText().length()<5||
                        userName.getText().length()>15,
                userId.textProperty(),
                password.textProperty()));
    }

    @FXML
    public void backToLogin(){
        UiBaseService.INSTANCE.getStageController().switchStage(R.id.LoginView,R.id.RegisterView);
    }

    @FXML
    public void onRegister(){
        RegisterUserReqVO registerUserReqVO =  new RegisterUserReqVO();
        registerUserReqVO.setUserId(userId.getText());
        registerUserReqVO.setPassword(password.getText());
        registerUserReqVO.setUserName(userName.getText());

        HttpUitl.getHttUtil().registUser(registerUserReqVO);
    }
}
