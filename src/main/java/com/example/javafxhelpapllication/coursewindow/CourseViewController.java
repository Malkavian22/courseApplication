package com.example.javafxhelpapllication.coursewindow;

import com.example.javafxhelpapllication.Course;
import com.example.javafxhelpapllication.ThemeView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CourseViewController {

    protected void initThemeView(Label name_theme, Course course) throws Exception {
        ThemeView themeView = new ThemeView(name_theme.getText(), course);
        themeView.start(new Stage());
    }
}
