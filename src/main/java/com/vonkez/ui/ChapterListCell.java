package com.vonkez.ui;

import com.jfoenix.controls.JFXButton;
import com.vonkez.Main;
import com.vonkez.controllers.BaseController;
import com.vonkez.model.Chapter;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ChapterListCell extends ListCell<Chapter> {
    FXMLLoader loader;
    BaseController controller;
    JFXButton button;
    Chapter currenChapter;

    public ChapterListCell() {
        try {
            loader = new FXMLLoader(Main.class.getResource("fxml/chapterCell.fxml"));
            button = (JFXButton) loader.load();
            button.setOnAction(event -> {
                System.out.println("Navigation here");
            });
            controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
        setGraphic(button);
    }

    @Override
    protected void updateItem(Chapter item, boolean empty) {
        currenChapter = item;
        super.updateItem(item, empty);
        if (item != null) {
            //setText(item.fullName);
            controller.initData(item);
            setGraphic(button);

        }
        else {
            setGraphic(null);
        }
    }
}
