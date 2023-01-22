module com.example.xmlparser_2_1_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jdom2;


    opens com.example.xmlparser_2_1_3 to javafx.fxml;
    exports com.example.xmlparser_2_1_3;
}