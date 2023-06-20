module com.example.proyect33 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proyect33 to javafx.fxml;
    exports com.example.proyect33;
}