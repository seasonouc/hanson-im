package com.hanson.im.client.ui.base;

import com.hanson.im.client.ui.StageController;

/**
 * @author hanson
 * @Date 2019/1/18
 * @Description:
 */
public class UiBaseService {

    public static UiBaseService INSTANCE = new UiBaseService();

    public StageController getStageController() {
        return stageController;
    }

    public StageController stageController = new StageController();


}
