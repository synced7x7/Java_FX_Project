module com.example.jumbledwords {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.jumbledwords to javafx.fxml;
    exports com.example.jumbledwords;
}