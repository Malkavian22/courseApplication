package com.example.javafxhelpapllication;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ThemeViewController {

    @FXML
    ScrollPane main_information;
    @FXML
    ProgressBar progress_page;
    @FXML
    ProgressBar progress_theme;
    @FXML
    VBox vbox_chapters;
    @FXML
    Label label_percent;
    @FXML
    Button hide;
    @FXML
    SplitPane split_pane1;

    static String current_label_name; //Имя открытого chapter'а.

    ArrayList<String> completedChapters = new ArrayList<>();

    double progress = 0.0;

    @FXML
    protected void onScroll(){

        double position = main_information.getVvalue();
        VBox vBox = (VBox)vbox_chapters.getChildren().get(0);
        double number_chapters = vBox.getChildren().size();


        if(position == 1.0 && !completedChapters.contains(current_label_name)){
            progress += position / number_chapters;
            completedChapters.add(current_label_name);

            progress_theme.setProgress(progress);
            label_percent.setText(progress * 100.0 + "%");
            colorCompleteChapter((int)number_chapters, vBox);
        }
    }

    protected void colorCompleteChapter(int i, VBox vBox){

        for(int j = 0; j < i; j++){

            HBox hBox = (HBox) vBox.getChildren().get(j); //получить HBox с кнопкой номера главы, и названием
            Label chapter_name = (Label) hBox.getChildren().get(1);

            if(chapter_name.getText().contains(current_label_name)){
                Button number_button = (Button) hBox.getChildren().get(0);
                number_button.setTextFill(Color.GREEN);
                chapter_name.setTextFill(Color.GREEN);
            }
        } }


    int clicked = 0;
    @FXML
    protected void onHideClick(){

        AnchorPane anchorPane1 = (AnchorPane) split_pane1.getItems().get(0);
        AnchorPane anchorPane2 = (AnchorPane) split_pane1.getItems().get(1);

        if(clicked % 2 == 0) {
            anchorPane1.maxWidthProperty().bind(split_pane1.widthProperty().multiply(0));
            anchorPane2.maxWidthProperty().bind(split_pane1.widthProperty().multiply(1));
        }
        else {
            anchorPane1.maxWidthProperty().bind(split_pane1.widthProperty().multiply(0.1088));
            anchorPane2.maxWidthProperty().bind(split_pane1.widthProperty().multiply(0.8912));
        }
        clicked++;

    }

}
