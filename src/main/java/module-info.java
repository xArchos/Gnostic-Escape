module com.example.gnosticescape_gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gnosticescape_gui to javafx.fxml;
    exports com.example.gnosticescape_gui;
}