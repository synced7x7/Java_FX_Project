module com.example.weatherapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.net.http;


    opens com.example.weatherapplication to javafx.fxml;
    exports com.example.weatherapplication;
}