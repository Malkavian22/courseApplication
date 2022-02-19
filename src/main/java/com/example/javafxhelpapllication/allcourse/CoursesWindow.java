package com.example.javafxhelpapllication.allcourse;

import com.example.javafxhelpapllication.Course;
import com.example.javafxhelpapllication.HelloApplication;
import com.example.javafxhelpapllication.XMLReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

public class CoursesWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CoursesWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 660, 900);
        Parent parent = scene.getRoot();
        ScrollPane courses = (ScrollPane) parent.lookup("#ScrollCourses");


        VBox box_courses = new VBox();

        VBox separators = new VBox();
        separators.setSpacing(30);
        separators.setPrefHeight(30);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPrefWidth(900);
        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        separator1.setPrefWidth(900);
        separators.getChildren().addAll(separator, separator1);
        box_courses.getChildren().add(separators);


        initCourses(box_courses);

        courses.setContent(box_courses);

        stage.setTitle("Обучающие курсы");
        stage.setScene(scene);
        stage.show();

    }
    //Сбор всех курсов воедино
    public void initCourses(VBox vBox){
        Course[] courses = new Course[0];
        try{
            courses = initCoursesInformation();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        for(Course course : courses){
            HBox course_line = initLine(course);
            vBox.getChildren().addAll(course_line,new Separator(Orientation.HORIZONTAL));
        }
    }

    //Инициализация одного курса
    private HBox initLine(Course course){

            HBox parent_Line;
            File file = new File("D:\\File\\test2.png");
            Image icon = new Image(file.toURI().toString());

            ImageView image = new ImageView(icon);
            image.setFitHeight(80);
            image.setFitWidth(90);

            Insets separator_margin = new Insets(0,-25,0,-25);
            Insets separator_padding = new Insets(-5,0,-5,0);

            Separator separator_first = new Separator(Orientation.VERTICAL);
            HBox.setMargin(separator_first,separator_margin);
            separator_first.setPadding(separator_padding);

            Separator separator_second = new Separator(Orientation.VERTICAL);
            HBox.setMargin(separator_second,separator_margin);
            separator_second.setPadding(separator_padding);

            Label course_name = new Label(course.getName());
            course_name.setPadding(new Insets(0,0,3,0));
            Label for_reading = new Label(course.getTime());
            Label course_description = new Label(course.getDescription());

            VBox about_course = new VBox(course_name, for_reading, course_description);
            about_course.setSpacing(4);
            about_course.setPadding(new Insets(2,0,0,5));
            about_course.setPrefHeight(90);
            about_course.setPrefWidth(330);

            Label user_progress = new Label((int) (100 * Double.valueOf(course.getProgress())) + "/100%");
            ProgressBar user_progressBar = new ProgressBar();
            user_progressBar.setProgress(Double.parseDouble(course.getProgress()));

            VBox about_progress = new VBox(user_progress, user_progressBar);
            about_progress.setSpacing(10);
            about_progress.setPadding(new Insets(0,4,0,4));
            HBox.setMargin(about_progress,new Insets(4,4,4,4));

            parent_Line = new HBox(image,separator_first, about_course, separator_second, about_progress);
            parent_Line.setSpacing(40);
            parent_Line.setPadding(new Insets(5,3,5,20));

            //ивент перехода на новое окно с передачей курса при нажатии на "линию" с курсом
            parent_Line.setOnMouseClicked(mouseEvent -> {
                CoursesWindowController CWC = new CoursesWindowController();
                CWC.setCourse(course);
                try {
                    CWC.test();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


        return parent_Line;
    }

    private Course[] initCoursesInformation() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        NodeList courses = XMLReader.getTags(XMLReader.COURSES, XMLReader.FILE);

        Course[] courses1 = new Course[courses.getLength()];

        for(int i = 0; i < courses.getLength(); i++){

            org.w3c.dom.Node course = courses.item(i);
            courses1[i] = new Course(
                    course.getAttributes().getNamedItem("name").getTextContent(),
                    course.getAttributes().getNamedItem("description").getTextContent(),
                    course.getAttributes().getNamedItem("photo").getTextContent(),
                    course.getAttributes().getNamedItem("progress").getTextContent(),
                    course.getAttributes().getNamedItem("reading_time").getTextContent()
            );
        }
        return courses1;
    }

}
