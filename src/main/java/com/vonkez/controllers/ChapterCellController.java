package com.vonkez.controllers;

import com.jfoenix.controls.JFXButton;
import com.vonkez.model.Chapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ChapterCellController implements Initializable, BaseController {
    @FXML
    private JFXButton button;

    @Override
    public void initData(Object data) {
        Chapter chapter = (Chapter)data;
        button.setText(chapter.name);


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
