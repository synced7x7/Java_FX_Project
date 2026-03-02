module com.example.scrambleword {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.scrambleword to javafx.fxml;
    exports com.example.scrambleword;
}