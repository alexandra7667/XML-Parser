module com.example.xmlparser {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jdom2;


    opens com.example.xmlparser to javafx.fxml;
    exports com.example.xmlparser;
}