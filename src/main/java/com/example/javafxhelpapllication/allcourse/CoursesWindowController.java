package com.example.javafxhelpapllication.allcourse;

import com.example.javafxhelpapllication.Course;
import com.example.javafxhelpapllication.coursewindow.CourseWindow;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.w3c.dom.Node;

public class CoursesWindowController {

    Course course;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @FXML
    protected void test() throws Exception {
        CourseWindow courseWindow = new CourseWindow();
        courseWindow.setCourse(course);
        courseWindow.start(new Stage());
    }
}
