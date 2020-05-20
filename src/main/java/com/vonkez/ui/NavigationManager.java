package com.vonkez.ui;

import com.vonkez.Main;
import com.vonkez.controllers.BaseController;
import com.vonkez.model.Manga;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.Instant;

public class NavigationManager {
    static StackPane contentPane;
    public static void initialize(StackPane pane){
        contentPane = pane;
    }

    public static void navigate(String query){
        var node = loadNode("searchResults", query);
    }
    public static void testNavigate(){
        var node = loadNode("searchResults", null);
    }

    public static void navigate(Manga manga){
        var node = loadNode("mangaInfo", manga);
    }

    private static Node loadNode(String pageName, Object controllerData) {
        long startTime = Instant.now().toEpochMilli();

        Node newNode = null;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/" + pageName + ".fxml"));

        try {
            newNode = loader.load();
            BaseController controller = loader.getController();

            ObservableList<Node> contentPaneChildren= contentPane.getChildren();
            contentPaneChildren.clear();
            contentPaneChildren.add(newNode);


            if (controllerData != null)
                controller.initData(controllerData);


        } catch (IOException e) {
            System.out.println("wwtf");
            e.printStackTrace();
        }
        System.out.println("loadNode time in milliseconds: " + (Instant.now().toEpochMilli() - startTime));

        return newNode;
    }
}
