package com.example.javafxhelpapllication.coursewindow;

import com.example.javafxhelpapllication.Course;
import com.example.javafxhelpapllication.HelloApplication;
import com.example.javafxhelpapllication.XMLReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

public class CourseWindow extends Application {

    Course node_course;

    private Course getCourse() {
        return node_course;
    }

    public void setCourse(Course course) {
        this.node_course = course;
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("course-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 900);
        Parent parent = scene.getRoot();
        ScrollPane course = (ScrollPane) parent.lookup("#course");

        NodeList nodeList = getThemes();

        VBox main_box = new VBox();

        for(int i = 0; i < nodeList.getLength(); i++ ) {
            Node oneThemes = nodeList.item(i);
            VBox box_theme = initTheme(oneThemes);
            HBox.setMargin(box_theme, new Insets(0, 0, 0, 20));
            box_theme.setPadding(new Insets(0, 0, 0, 0));
            main_box.getChildren().add(box_theme);
        }
        course.setContent(main_box);
        System.out.println(node_course.getName());

        stage.setTitle("Курс X");
        stage.setScene(scene);
        stage.show();
    }

    private VBox initTheme(Node oneThemes){

        VBox box_theme;

        VBox header_separator;
        AnchorPane header;

        Label header_label = new Label(oneThemes.getAttributes().getNamedItem("header").getTextContent());
        AnchorPane.setLeftAnchor(header_label, 50.0);
        AnchorPane.setTopAnchor(header_label, 5.0);

        Button button = new Button("");
        AnchorPane.setRightAnchor(button, 300.0);
        AnchorPane.setTopAnchor(button, 5.0);
        AnchorPane.setBottomAnchor(button,2.0);

        header = new AnchorPane(header_label, button);
        header.setPrefWidth(1200);

        header_separator = new VBox(header, new Separator(Orientation.HORIZONTAL));
        VBox partTheme = initPartTheme(oneThemes);
        box_theme = new VBox(header_separator, partTheme, new Separator(Orientation.HORIZONTAL));

        /**
         * Скрипт для "сворачивания и разворачивания курса"
         */
        button.setOnMouseClicked(mouseEvent -> {

            if(box_theme.getChildren().contains(partTheme))
                box_theme.getChildren().remove(partTheme);
            else box_theme.getChildren().add(partTheme);

        });

        return box_theme;
    }

    private VBox initPartTheme(Node node_themes) {

        NodeList themeschild = node_themes.getChildNodes();
        for(int i = 0; i < themeschild.getLength(); i++){
            Node node = themeschild.item(i);

            if(node.getNodeType()!=Node.ELEMENT_NODE) node_themes.removeChild(node);
        }

        VBox themes = new VBox();
        themes.setPrefWidth(1200);
        themes.setSpacing(5);

        File file = new File("D:\\File\\test2.png");
        Image icon = new Image(file.toURI().toString());
        themeschild = node_themes.getChildNodes();

        for(int i = 0; i < themeschild.getLength(); i++){
            Node theme = themeschild.item(i);
            HBox part;

            ImageView image = new ImageView(icon);

            VBox desc; //layout для описания темы, задание параметров для его элементов
            Label name_theme = new Label(theme.getAttributes().getNamedItem("name").getTextContent());
            Label type = new Label(theme.getAttributes().getNamedItem("type").getTextContent());
            desc = new VBox(name_theme, type);
            desc.setSpacing(10);
            desc.setPrefHeight(80);
            desc.setMinWidth(330);
            desc.setPrefWidth(600);
            desc.setMaxWidth(5000);

            String progress = theme.getAttributes().getNamedItem("progress").getTextContent();

            Label is_Completed = new Label(progress.equals("1.0") ? "ЗАВЕРШЕНО" : "НЕ ЗАВЕРШЕНО");

            Separator verticalSeparator = new Separator(Orientation.VERTICAL);
            Separator verticalSeparator2 = new Separator(Orientation.VERTICAL);
            Insets insets_Separator = new Insets(-12,0,-11,0);
            verticalSeparator.setPadding(insets_Separator);
            verticalSeparator2.setPadding(insets_Separator);

            part = new HBox(image, verticalSeparator, desc, verticalSeparator2, is_Completed);
            part.setSpacing(40);
            part.setPadding(new Insets(10,3,2,10));

            Separator separator = new Separator(Orientation.HORIZONTAL);
            separator.setMaxWidth(5000);
            separator.setPrefWidth(900);
            separator.setMinWidth(600);

            themes.getChildren().addAll(part,separator); //добавить тему к другим темам + разделитель,
            /**
             * открыть одну из тем курса
             */

            part.setOnMouseClicked(mouseEvent -> {
                try {
                   CourseViewController courseViewController = new CourseViewController();
                   courseViewController.initThemeView(name_theme, getCourse());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return themes;
    }

    /**
     * Для получения тэга с заголовком (названием) Большой темы
     * @throws XPathExpressionException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private NodeList getThemes() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        NodeList themes = (NodeList) XMLReader.getTags(XMLReader.
                        getAllThemesCourse("[@name='" + node_course.getName() + "']"), XMLReader.FILE);
        return themes;
    }

    /**
     * Получение тэга <theme></> - подтемы
     * @param themes_name
     * @throws XPathExpressionException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private NodeList getTheme(String themes_name) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        NodeList themes = (NodeList) XMLReader.getTags(XMLReader.
                getAllThemesCourse("[@name='" + themes_name + "']"), XMLReader.FILE);
        return themes;
    }
}
