package com.vonkez.ui;

import com.vonkez.Main;
import com.vonkez.controllers.BaseController;
import com.vonkez.model.Manga;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayDeque;

public class NavigationManager {
    static StackPane contentPane;
    static ArrayDeque<Node> history;
    static int historyLimit;
    static Node currentNode;

    public static void initialize(StackPane pane){
        contentPane = pane;
        history = new ArrayDeque<Node>();
        historyLimit = 5;
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

    public static void navigateBack() {
        var previousNode = history.pop();
        var contentPaneChildren = contentPane.getChildren();
        contentPaneChildren.removeIf(node -> !(node instanceof Button));
        contentPaneChildren.add(previousNode);
        currentNode = previousNode;

        // show backbutton again
        contentPane.getChildren().get(0).toFront();
    }

    public static boolean canNavigateBack() {
        return !history.isEmpty();
    }

    private static Node loadNode(String pageName, Object controllerData) {
        long startTime = Instant.now().toEpochMilli();

        Node newNode = null;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/" + pageName + ".fxml"));

        try {
            newNode = loader.load();
            BaseController controller = loader.getController();

            ObservableList<Node> contentPaneChildren= contentPane.getChildren();
            contentPaneChildren.removeIf(node -> !(node instanceof Button));
            contentPaneChildren.add(newNode);


            if (controllerData != null)
                controller.initData(controllerData);


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("loadNode time in milliseconds: " + (Instant.now().toEpochMilli() - startTime));

        // history
        if  (currentNode != null){
            history.push(currentNode);
        }

        currentNode = newNode;


        if (history.size() > historyLimit) {
            history.removeLast();
        }
        // show backbutton again
        contentPane.getChildren().get(0).toFront();


        return newNode;
    }
}
