package com.example.javafxhelpapllication;

import com.example.javafxhelpapllication.allcourse.CoursesWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label aboutCompany;
    @FXML
    private Button buttonNewWindow;

    @FXML
    protected void createNewWindow() throws Exception {
        //Закрываем текущее окно
        Stage stage = (Stage) buttonNewWindow.getScene().getWindow();
        stage.close();
        new CoursesWindow().start(new Stage());
    }
}