module com.example.javafxhelpapllication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;


    opens com.example.javafxhelpapllication to javafx.fxml;
    exports com.example.javafxhelpapllication;
    exports com.example.javafxhelpapllication.coursewindow;
    opens com.example.javafxhelpapllication.coursewindow to javafx.fxml;
    exports com.example.javafxhelpapllication.allcourse;
    opens com.example.javafxhelpapllication.allcourse to javafx.fxml;
}