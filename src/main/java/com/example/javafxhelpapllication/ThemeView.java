package com.example.javafxhelpapllication;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;


public class ThemeView extends Application {

    String nameTheme = "";
    Course current_course;
    ScrollPane main_scrollPane;
    NodeList nodeListChapter;
    ProgressBar progress_page;
    ProgressBar progress_theme;

    XMLReader xmlReader;


    public ThemeView(String nameTheme, Course current_course){
        this.nameTheme = nameTheme;
        this.current_course = current_course;
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(ThemeView.class.getResource("theme-view.fxml"));
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        Scene scene = new Scene(fxmlLoader.load(), 1500, dimension.height - 50);
        Parent parent = scene.getRoot();
        xmlReader = new XMLReader();

        nodeListChapter = XMLReader.getTags(getAllChapterTheme("[@name = '" + current_course.getName() + "']",
                "[@name = '" + nameTheme + "']"), XMLReader.FILE);

        SplitPane splitPane = (SplitPane) parent.lookup("#split_pane1");

        AnchorPane anchorPane1 = (AnchorPane) splitPane.getItems().get(0);
        anchorPane1.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.1088)); //фиксированное значение разделяющей линии
        VBox vbox = (VBox) anchorPane1.getChildren().get(0);

        VBox theme_name_layout = (VBox) vbox.getChildren().get(0);
        ((Label)theme_name_layout.getChildren().get(0)).setText(current_course.getName());

        HBox progressBar_layout = (HBox) theme_name_layout.getChildren().get(1);

        progress_theme = (ProgressBar) progressBar_layout.getChildren().get(1);


        VBox vbox_chapters = (VBox) vbox.getChildren().get(1);
        vbox_chapters.getChildren().addAll(
                initChaptersList(nodeListChapter));

        AnchorPane anchorPane2 = (AnchorPane) splitPane.getItems().get(1);
        anchorPane2.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.8912)); //фиксированное значение разделяющей линии

        SplitPane splitPane2 = (SplitPane) anchorPane2.lookup("#split_pane2");
        AnchorPane Top_Achor = (AnchorPane) splitPane2.getItems().get(splitPane2.getItems().size()-2);
        VBox topVBox = (VBox) Top_Achor.lookup("#top_vbox");
        progress_page = (ProgressBar) topVBox.lookup("#progress_page");
        AnchorPane main_anchor = (AnchorPane) splitPane2.getItems().get(splitPane2.getItems().size()-1);

        main_scrollPane = (ScrollPane) main_anchor.lookup("#main_information");

        initChapter(nodeListChapter.item(0),1); //загрузка первой главы, при загрузке окна с главами

        stage.setTitle(nameTheme);
        stage.setScene(scene);
        stage.show();
        //обработчик события закрытия окна
        stage.setOnCloseRequest(event -> {

            try {
                xmlReader.updateProgressNode(progress_theme, nameTheme);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        });

        Thread progressChanger = new Thread(new ProgressBarChanger(progress_page,main_scrollPane));
        progressChanger.setDaemon(true);
        progressChanger.start();

    }

    private VBox initChaptersList(NodeList nodeListChapter){

        VBox chapters = new VBox();
        chapters.setId("CHAPTERS");

        for(int i = 0; i < nodeListChapter.getLength(); i++){

            org.w3c.dom.Node chapter = nodeListChapter.item(i);
            HBox hBox = new HBox();
            Label chapter_name = new Label(chapter.getAttributes().getNamedItem("name").getTextContent());
            Button number_chapter = new Button(String.valueOf(i+1));

            hBox.getChildren().addAll(number_chapter, chapter_name);
            hBox.setSpacing(10);
            chapters.getChildren().add(hBox);
            if(i == 0) ThemeViewController.current_label_name = chapter_name.getText();

            int final_I = i + 1;
            hBox.setOnMouseClicked(mouseEvent -> { //скрипт для загрузки контента главы, при нажатии в списке
                initChapter(chapter, final_I);
                main_scrollPane.setVvalue(0);
            });
        }
        chapters.setSpacing(8);

        return chapters;

    }

    private void initChapter(org.w3c.dom.Node chapter, int i){

        main_scrollPane.setContent(null);

        VBox chapterAndName = new VBox();
        Label chapterLabel = new Label("Глава " + i);
        chapterLabel.setFont(new Font("Arial",22));
        chapterLabel.setPrefHeight(15);


        Label name = new Label(chapter.getAttributes().getNamedItem("name").getTextContent()); // название главы
        ThemeViewController.current_label_name = name.getText();

        name.setPrefHeight(50);
        name.setWrapText(true);
        name.setFont(new Font("Arial",20));
        chapterAndName.setSpacing(30);
        chapterAndName.getChildren().addAll(chapterLabel, name);
        chapterAndName.setFillWidth(true);
        chapterAndName.setPrefHeight(100);
        chapterAndName.setPadding(new Insets(20, 10, 0, 450));

        NodeList contentList = chapter.getChildNodes();

        VBox vBox = new VBox(chapterAndName);

        Font font_content = new Font("Arial",16);

        for(int j = 0; j < contentList.getLength(); j++){

            org.w3c.dom.Node content = contentList.item(j);
            if(content.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) continue;

            Label content_text = new Label(content.getTextContent());
            content_text.setFont(font_content);
            content_text.setWrapText(true);
            content_text.prefWidthProperty().bind(main_scrollPane.prefWidthProperty());
            content_text.setPadding(new Insets(0, 20, 0, 10));
            content_text.setTextAlignment(TextAlignment.JUSTIFY);


            StackPane stackPaneforImage = new StackPane();
            String pathImage = content.getAttributes().getNamedItem("photo").getTextContent();
            File file = new File(pathImage);
            javafx.scene.image.Image icon = new Image(file.toURI().toString());

            ImageView image = new ImageView(icon);

            stackPaneforImage.setAlignment(Pos.CENTER);
            image.setFitHeight(500);
            image.setFitWidth(500);

            String descPhoto = "";
            StackPane stackPaneDesc = new StackPane();
            try{
                descPhoto = content.getAttributes().getNamedItem("description").getTextContent();
                Label desc = new Label("Рис. " + descPhoto);
                desc.setWrapText(true);
                desc.setFont(new Font("Arial",15));
                stackPaneDesc.getChildren().add(desc);
                stackPaneDesc.setAlignment(Pos.CENTER);
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            stackPaneforImage.getChildren().addAll(image);
            //stackPaneforImage.setPadding(new Insets(0,10,0,200));

            vBox.getChildren().addAll(content_text,stackPaneforImage,stackPaneDesc);
        }

        vBox.setSpacing(15);
        vBox.setFillWidth(true);

        String next_button_string = (nodeListChapter.getLength() == i) ? "Завершить" : "Далее" ;
        Button next_button = new Button(next_button_string);
        StackPane stackPaneButton = new StackPane();
        stackPaneButton.setAlignment(Pos.CENTER);
        stackPaneButton.getChildren().add(next_button);

        next_button.setOnMouseClicked(mouseEvent -> {
            if(next_button.getText().contains("Далее")){  //скрипт для кнопки перехода на новую главу
                initChapter(nodeListChapter.item(i),i+1);
                main_scrollPane.setVvalue(0);
            }
            else{
                Stage stage = (Stage) next_button.getScene().getWindow();
                try {
                    xmlReader.updateProgressNode(progress_theme, nameTheme);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
                stage.close();
            }
        });
        vBox.getChildren().add(stackPaneButton);


        main_scrollPane.setContent(vBox);
    }


    /**
     * @param filter_theme фильтр для тэга <theme>
     * @return путь в xml на тэг со всеми главами текущей темы по фильтру
     */
    public static String getAllChapters(String filter_course, String filter_theme){
        return "//course" + filter_course + "/themes/theme" + filter_theme + "/chapters";
    }
    public static String getAllChapterTheme(String filter_course, String filter_theme){
        return "//course" + filter_course + "/themes/theme" + filter_theme + "/chapters/chapter";
    }
    public static String getAllContent(String filter){
        return "//chapter" + filter + "/content";
    }



}
