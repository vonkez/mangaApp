module mangaApp {
    requires transitive  javafx.controls;
    requires transitive  javafx.fxml;
    requires transitive javafx.graphics;

    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.ionicons4;
    requires com.jfoenix;
    requires io.reactivex.rxjava2;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires rxjavafx;
    requires okhttp3;
    requires org.jsoup;

    opens com.vonkez to javafx.fxml;
    opens com.vonkez.controllers to javafx.fxml;
    exports com.vonkez;
}