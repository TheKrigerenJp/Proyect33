module com.example.proyect33 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.proyect33 to javafx.fxml;
    exports com.example.proyect33;
}