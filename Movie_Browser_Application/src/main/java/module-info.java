module com.example.movie_browser_application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.movie_browser_application to javafx.fxml;
    exports com.example.movie_browser_application;
    exports model;
    opens model to javafx.fxml;
}