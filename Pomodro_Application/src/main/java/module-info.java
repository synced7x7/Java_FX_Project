module com.example.pomodro_application {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.pomodro_application to javafx.fxml;
    exports com.example.pomodro_application;
}