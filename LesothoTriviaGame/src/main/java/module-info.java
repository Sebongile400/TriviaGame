module com.example.training {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.training to javafx.fxml;
    exports com.example.training;
}