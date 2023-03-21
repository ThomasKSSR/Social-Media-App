module com.example.lab08mapgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.lab08mapgui to javafx.fxml;
    exports com.example.lab08mapgui;
    opens Controller to javafx.fxml;
    exports Controller;

    opens Domain;
    exports Domain to javafx.fxml;

}